package digital.erben.reactiveweb.cities;

import digital.erben.reactiveweb.cities.model.City;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Provides Cities data from a local file.
 */
@Component
public class CitiesService {

    private final CitiesDataSource citiesDataset;

    public CitiesService(CitiesDataSource citiesDataset) {
        this.citiesDataset = citiesDataset;
    }

    /**
     * Retrieves a Flux of cities with a specified limit from the cached dataset.
     *
     * @param limit The maximum number of cities to retrieve.
     * @return A Flux of GeoDataModel.City objects with the specified limit.
     */
    public Flux<City> getCitiesWithLimit(long limit) {
        return Flux.fromStream(this.citiesDataset.loadCities())
            .take(limit);
    }

    /**
     * Retrieves a Flux of cities with a population greater than or equal to the specified cutoff (inclusive).
     *
     * @param cutoffInclusive The minimum population cutoff value.
     * @return A Flux of GeoDataModel.City objects with population greater than or equal to the cutoff.
     */
    public Flux<City> getCitiesWithPopulationGreaterThanInclusive(long cutoffInclusive) {
        return Flux.fromStream(citiesDataset.loadCities())
            .filter(city -> city.population() >= cutoffInclusive);
    }

    /**
     * Retrieves a Flux of cities sorted by population in ascending order.
     *
     * @return A Flux of GeoDataModel.City objects sorted by population in ascending order.
     */
    public Flux<City> getCitiesSortedByPopulationAscending() {
        return Flux.fromStream(citiesDataset.loadCities())
            .sort(Comparator.comparingInt(City::population));
    }

    /**
     * Retrieves the first city that matches the given predicate from the cached dataset.
     *
     * @param predicate The predicate to match the city against.
     * @return A Mono that emits the first GeoDataModel.City object that matches the predicate.
     */
    public Mono<City> getFirstCityThatMatches(Predicate<City> predicate) {
        return Flux.fromStream(citiesDataset.loadCities())
            .filter(predicate)
            .next();
    }

}
