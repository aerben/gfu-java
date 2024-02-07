package digital.erben.reactiveweb.cities.model;

import java.util.List;

public record CitiesResponse(
    List<City> data,
    List<Link> links,
    Metadata metadata
) {
}