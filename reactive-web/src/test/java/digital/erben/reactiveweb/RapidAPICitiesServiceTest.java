package digital.erben.reactiveweb;

import digital.erben.reactiveweb.cities.RapidAPICitiesService;
import digital.erben.reactiveweb.cities.model.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RapidAPICitiesServiceTest {

    private RapidAPICitiesService remoteGeoDataService;

    @BeforeEach
    public void setupService() {
        this.remoteGeoDataService = new RapidAPICitiesService();
    }

    @Test
    void retrieveCitiesForCountry() {
        List<City> result = remoteGeoDataService.retrieveCitiesForCountry("DE");
        assertFalse(result.isEmpty());
    }

    @Test
    void retrieveCitiesForCountryAsync() {
        Mono<List<City>> de = remoteGeoDataService.retrieveCitiesForCountryAsync("DE");
        StepVerifier.create(de)
            .expectNextCount(1)
            .verifyComplete();

    }
}