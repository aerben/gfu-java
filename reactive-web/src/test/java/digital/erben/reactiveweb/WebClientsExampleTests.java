package digital.erben.reactiveweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digital.erben.reactiveweb.cities.model.CitiesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class WebClientsExampleTests {
    public static final String URL = "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds=DE&minPopulation=200000&limit=100";
    private final ObjectMapper objectMapper;

    public WebClientsExampleTests() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void callApiViaJavaHttpClient() throws IOException, InterruptedException {
        Path cities = Files.createTempFile("cities", ".txt");
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL))
            .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
            .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<Path> response = HttpClient.newHttpClient().send(request,
            HttpResponse.BodyHandlers.ofFile(cities)
        );
    }

    @Test
    public void callApiViaRestTemplate() throws JsonProcessingException {
        final String url = URL;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48");
        headers.set("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<CitiesResponse> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            CitiesResponse.class
        );
        CitiesResponse model = response.getBody();
        System.out.println(objectMapper.writeValueAsString(model));
    }

    @Test
    public void callApiViaRestClient() throws JsonProcessingException {
        var restClient = RestClient.create();
        var res = restClient.get()
            .uri(URI.create(URL))
            .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
            .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
            .retrieve().toEntity(CitiesResponse.class);
        System.out.println(objectMapper.writeValueAsString(res.getBody()));
    }

    @Test
    public void callApiViaWebClient() throws JsonProcessingException, InterruptedException {
        var restClient = WebClient.create();
        restClient.get()
            .uri(URI.create(URL))
            .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
            .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
            .retrieve()
            .toEntity(CitiesResponse.class)
            .mapNotNull(resp -> resp.getBody().data());

        Thread.sleep(10000L);
    }

    @Test
    public void callApiViaWebClientWithFullErrorHandling() throws JsonProcessingException, InterruptedException {
        var restClient = WebClient.create();
        restClient.get()
            .uri(URI.create(URL))
            .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48s")
            .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
            .retrieve()
            .onStatus(code(404), response -> Mono.just(new NotFound(response)))
            .onStatus(code(403), response -> Mono.just(new Forbidden(response)))
            .onStatus(code(400), response -> Mono.just(new BadRequest(response)))
            .toEntity(CitiesResponse.class)
            .mapNotNull(HttpEntity::getBody)
            .map(CitiesResponse::data)
            .subscribe(
                System.out::println,
                this::handleException
            );

        Thread.sleep(10000L);
    }

    private void handleException(Throwable throwable) {
        // you would now insert your error handling logic depending on the use case
        // either as switch expression like here, or a series of instanceof checks.
        var databaseRow = switch (throwable) {
            case BadRequest br -> new DatabaseRow(br.getOffendingDhlField());
            case Forbidden fb -> new DatabaseRow(fb.getReason());
            case NotFound nf -> new DatabaseRow(nf.getURI().toString());
            default -> new DatabaseRow("Known error occured");
        };
        System.err.println(databaseRow);
    }

    // That would likely be a JPA entity instead
    record DatabaseRow(String reason){}

    static class DhlApiException extends RuntimeException {
        private final ClientResponse clientResponse;

        DhlApiException(ClientResponse clientResponse) {
            this.clientResponse = clientResponse;
        }

        public ClientResponse getClientResponse() {
            return clientResponse;
        }
    }

    final class NotFound extends DhlApiException {
        NotFound(ClientResponse clientResponse) {
            super(clientResponse);
        }
        URI getURI() {
            return getClientResponse().request().getURI();
        }
    }

    final class Forbidden extends DhlApiException {
        Forbidden(ClientResponse clientResponse) {
            super(clientResponse);
        }
        String getReason() {
            // Determine why the request was forbidden
            return "wrong api key";
        }
    }

    final class BadRequest extends DhlApiException {
        BadRequest(ClientResponse clientResponse) {
            super(clientResponse);
        }
        String getOffendingDhlField() {
            // Logic to parse the offending field from ClientResponse
            return "zipcode";
        }
    }


    static Predicate<HttpStatusCode> code(int code) {
        return (httpStatusCode -> httpStatusCode.value() == code);
    }
}
