package digital.erben.movies;

import java.util.stream.Stream;

public interface MovieRatingsDataset {
    Stream<MovieRating> load();

}

