package digital.erben.reactiveweb.cities;

import digital.erben.reactiveweb.cities.model.City;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
public class CitiesController {

    private final CitiesDataSource ds;

    public CitiesController(CitiesDataSource ds) {
        this.ds = ds;
    }

    /**
     * Get cities from the dataset
     * @param population optional parameter. If given, restricts the returned cities to ones with population greater than the given value
     * @return Cities from the dataset
     */
    @GetMapping("/cities")
    public Flux<City> getAllCities(
        @RequestParam(value = "fromPopulation", required = false) Long population
    ) {
       return Flux.fromStream(ds.loadCities())
           .filter(city -> population == null || population < city.population());
    }

}
