package digital.erben.movies;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MovieRatingsServiceTest {

    private MovieRatingsService movieRatingsService;
    private final MovieRatingsDataset mockDataset = MovieRatingsDataset.getInstance();

    MovieRatingsServiceTest() {
        movieRatingsService = new MovieRatingsService(mockDataset);
    }


    @Test
    void getRatingsTest() {
        long limit = 2;
        List<MovieRating> result = movieRatingsService.getRatings(limit).toList();
        List<MovieRating> firstTwoFromDs = mockDataset.load().limit(2).toList();
        Assertions.assertEquals(result, firstTwoFromDs);
    }

}
