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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

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
}
