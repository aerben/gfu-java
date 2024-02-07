package digital.erben.reactiveweb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digital.erben.reactiveweb.cities.model.CitiesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class NewWebClientsExampleTests {
    public static final String URL = "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds=DE&minPopulation=200000&limit=100";
    private static final String RAPID_APIKEY_HEADER = "X-RapidAPI-Key";
    private static final String RAPID_APIKEY_VALUE = "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48";
    private static final String RAPID_APIHOST_HEADER="X-RapidAPI-Host";
    private static final String RAPID_APIHOST_VALUE="wft-geo-db.p.rapidapi.com";

    private final ObjectMapper objectMapper;

    public NewWebClientsExampleTests() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void callApiViaJavaHttpClient() throws IOException, InterruptedException {
        URI uri = URI.create(
            "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds=DE&minPopulation=300000&maxPopulation=320000&namePrefix=Mann");
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
            .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
            .GET()
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    @Test
    public void callApiViaRestTemplate() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key","16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48");
        headers.add("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com");
        headers.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<>("", headers);

        ResponseEntity<CitiesResponse> response = restTemplate.exchange(
            "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds=DE&minPopulation=300000&maxPopulation=320000&namePrefix=Mann",
            HttpMethod.GET,
            entity,
            CitiesResponse.class
        );

        System.out.println(objectMapper.writeValueAsString(response.getBody()));

    }

    @Test
    public void callApiViaRestClient() throws JsonProcessingException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                ResponseEntity<CitiesResponse> entity = RestClient.create()
                    .get()
                    .uri(URI.create(
                        "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds=DE&minPopulation=300000&maxPopulation=320000&namePrefix=Mann"))
                    .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
                    .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                    .retrieve()
                    .toEntity(CitiesResponse.class);
                System.out.println(entity.getBody());
            });
        }
        Thread.sleep(10000L);
    }


    sealed interface DhlError permits ForbiddenError, NotFoundError{}

    record ForbiddenError() implements DhlError{}

    record NotFoundError() implements DhlError{}

    public static class DhlApiException extends RuntimeException {

        private final DhlError dhlError;

        public DhlApiException(DhlError error) {
            this.dhlError =error;
        }

        public DhlError getDhlError() {
            return dhlError;
        }
    }

    @Test
    public void callApiViaWebClient() throws JsonProcessingException, InterruptedException {
        Predicate<HttpStatusCode> is404 = httpStatusCode -> httpStatusCode.value() == 404;
        Predicate<HttpStatusCode> is403 = httpStatusCode -> httpStatusCode.value() == 403;

        WebClient webClient = WebClient.create();

        Flux.just("DE", "EN", "US")

            .map(country -> webClient
                .get()
                .uri(URI.create(
                    "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds="+country+"&minPopulation=300000&maxPopulation=320000&namePrefix=Mann"))
                .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
                .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                .retrieve()
                .onStatus(is404, clientResponse -> Mono.just(new DhlApiException(new NotFoundError())))
                .onStatus(is403, clientResponse -> Mono.just(new DhlApiException(new ForbiddenError())))
                .toEntity(CitiesResponse.class)
            );

        Thread.sleep(10000L);
    }

    private void printErrorToSystemErr(Throwable throwable) {
        System.err.println("------------");
        System.err.println(throwable.getMessage());
    }

    private void printResultToSystemOut(ResponseEntity<CitiesResponse> response) {
            System.out.println(response.getBody());
    }
}
