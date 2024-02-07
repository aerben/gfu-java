package digital.erben.reactiveweb.cities;

import com.fasterxml.jackson.databind.ObjectMapper;
import digital.erben.reactiveweb.cities.model.CitiesResponse;
import digital.erben.reactiveweb.cities.model.City;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class CitiesDataSource {

    public Stream<City> loadCities() {
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("cities.json");
            Objects.requireNonNull(resourceAsStream);
            CitiesResponse geoData = new ObjectMapper().readValue(resourceAsStream, CitiesResponse.class);
            return geoData.data().stream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
