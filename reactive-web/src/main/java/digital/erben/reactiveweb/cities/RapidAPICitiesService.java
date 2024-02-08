package digital.erben.reactiveweb.cities;

import digital.erben.reactiveweb.cities.model.CitiesResponse;
import digital.erben.reactiveweb.cities.model.City;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Uses RapidAPI to fetch latest city data
 */
@Component
public class RapidAPICitiesService {

    private final String url;
    private final Map<String, String> rapidUrlHeaders;

    public RapidAPICitiesService() {
        this.url = "https://wft-geo-db.p.rapidapi.com/v1/geo/cities";
        this.rapidUrlHeaders = Map.of(
            "X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48",
            "X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com");
    }


    /**
     * Retrieves a list of cities for a given country code.
     *
     * @param countryCode the country code
     * @return a list of cities for the given country code
     */
    public List<City> retrieveCitiesForCountry(String countryCode) {
        var restClient = RestClient.create();
        return restClient.get()
            .uri(URI.create(
                "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds=" + countryCode + "&minPopulation=500000"))
            .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
            .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
            .retrieve()
            .toEntity(CitiesResponse.class)
            .getBody().data();
    }

    /**
     * Retrieves a list of cities for a given country code asynchronously.
     *
     * @param countryCode the country code
     * @return a Mono emitting a list of cities for the given country code
     */
    public Mono<List<City>> retrieveCitiesForCountryAsync(String countryCode) {
        var restClient = WebClient.create();
        return restClient.get()
            .uri(URI.create(
                "https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds=" + countryCode + "&minPopulation=500000"))
            .header("X-RapidAPI-Key", "16e1546221msh6270284dc1921b8p16483fjsnf33be4140b48")
            .header("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
            .retrieve()
            .bodyToMono(CitiesResponse.class)
            .map(CitiesResponse::data);
    }

}
