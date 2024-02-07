package digital.erben.reactiveweb;

import digital.erben.reactiveweb.cities.CitiesController;
import digital.erben.reactiveweb.cities.CitiesDataSource;
import digital.erben.reactiveweb.cities.model.City;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

@WebFluxTest(controllers = CitiesController.class)
public class CitiesControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CitiesDataSource citiesDataset;

    @Test
    public void testGetAllCities() {
        City testCity = new City(1, "Q64", "city", "Berlin", "Berlin", "Germany", "DE", "Berlin", "BE", "Q64", 52.5200, 13.4050, 3769495);
        Mockito.when(citiesDataset.loadCities()).thenReturn(Stream.of(testCity));

        webTestClient.get().uri("/cities")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(City.class)
                .contains(testCity);
    }


    @Test
    public void testGetAllCitiesGreaterThanPopulation() {
        City testCity = new City(1, "Q64", "city", "Berlin", "Berlin", "Germany", "DE", "Berlin", "BE", "Q64", 52.5200, 13.4050, 3769495);
        City testCity2 = new City(1, "Q64", "city", "Berlin", "Berlin", "Germany", "DE", "Berlin", "BE", "Q64", 52.5200, 13.4050, 12);
        City testCity3 = new City(1, "Q64", "city", "Berlin", "Berlin", "Germany", "DE", "Berlin", "BE", "Q64", 52.5200, 13.4050, 13);

        Mockito.when(citiesDataset.loadCities()).thenReturn(Stream.of(testCity, testCity2, testCity3));

        webTestClient.get().uri("/cities?fromPopulation=1000")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(City.class)
            .hasSize(1)
            .contains(testCity);
    }
}
