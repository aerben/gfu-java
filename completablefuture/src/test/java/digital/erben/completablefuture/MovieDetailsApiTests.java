package digital.erben.completablefuture;

import org.junit.jupiter.api.Test;

public class MovieDetailsApiTests {

    @Test
    public void loadDetailsForMovie_shouldThrowIllegalArgumentException_WhenNullValueIsPassed() {
        // Mocks.movieDetailsApiMock().loadDetailsForMovie(null); // implement further
    }

    @Test
    public void loadDetailsForMovie_shouldLoadBarbieMovieWhenBarbieIsPassedAsTitle() {
        //Mocks.movieDetailsApiMock().loadDetailsForMovie("Barbie"); // implement checks
    }

    @Test
    public void usingOppenheimerAsNameShouldTimeoutAfter3Seconds() {
        //Mocks.movieDetailsApiMock().loadDetailsForMovie("Oppenheimer").join();
    }
}
