package digital.erben.reactiveweb;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digital.erben.reactiveweb.cities.model.CitiesResponse;
import digital.erben.reactiveweb.cities.model.City;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("UnnecessaryLocalVariable")
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
        WebClient.create().get()
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
        WebClient.create().get()
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

    static final class NotFound extends DhlApiException {
        NotFound(ClientResponse clientResponse) {
            super(clientResponse);
        }
        URI getURI() {
            return getClientResponse().request().getURI();
        }
    }

    static final class Forbidden extends DhlApiException {
        Forbidden(ClientResponse clientResponse) {
            super(clientResponse);
        }
        String getReason() {
            // Determine why the request was forbidden
            return "wrong api key";
        }
    }

    static final class BadRequest extends DhlApiException {
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

    @Test
    public void callApiViaWebClientFromAFluxWithRetry() throws JsonProcessingException, InterruptedException {
        var webClient = WebClient.create();

        Flux<String> countries = Flux.just("DE", "FR", "IT");

        Flux<City> cityFlux = countries.flatMap(country -> {
                Mono<List<City>> monoList = webClient.get()
                    .uri(URI.create(URL))
                    .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
                    .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                    .retrieve()
                    .bodyToMono(CitiesResponse.class) // kürzer als toEntity wenn man den Response Code nicht braucht
                    .mapNotNull(CitiesResponse::data);
                Flux<City> flattenedFlux = monoList.flatMapMany(Flux::fromIterable); // macht aus Mono<List<?>> ein Flux<?>
                return flattenedFlux;
            }
        ).retryWhen(Retry.backoff(3, Duration.ofMinutes(1))); // on failure, retries automatically

        cityFlux.subscribe(System.out::println);


        Thread.sleep(10000L);
    }


    @Test
    public void callApiViaWebClientFromAFluxWithRateLimit() throws JsonProcessingException, InterruptedException {
        var webClient = WebClient.create();
        RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(10))
            .limitForPeriod(2) // 10 requests per second
            .timeoutDuration(Duration.ofSeconds(5))
            .build();

        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = registry.rateLimiter("countriesRateLimiter");


        Flux<String> countries = Flux.just("DE", "FR", "IT");

        Flux<City> cityFlux = countries.flatMap(country -> {
                Mono<List<City>> monoList = webClient.get()
                    .uri(URI.create(URL))
                    .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
                    .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                    .retrieve()
                    .bodyToMono(CitiesResponse.class) // kürzer als toEntity wenn man den Response Code nicht braucht
                    .mapNotNull(CitiesResponse::data);
                Flux<City> flattenedFlux = monoList.flatMapMany(Flux::fromIterable); // macht aus Mono<List<?>> ein Flux<?>
                return flattenedFlux;
            }
        ).transformDeferred(RateLimiterOperator.of(rateLimiter));

        cityFlux.subscribe(System.out::println);


        Thread.sleep(10000L);
    }
}
