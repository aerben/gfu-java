package digital.erben.completablefuture;

import org.junit.jupiter.api.Test;

public class MovieDetailsApiTests {

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenNullValueIsPassed() {
        // Mocks.movieDetailsApiMock().loadDetailsForMovie(null); // implement further
    }

    @Test
    public void shouldLoadBarbieMovieWhenBarbieIsPassedAsTitle() {
        //Mocks.movieDetailsApiMock().loadDetailsForMovie("Barbie"); // implement checks
    }

    @Test
    public void usingOppenheimerAsNameShouldTimeoutAfter3Seconds() {
        //Mocks.movieDetailsApiMock().loadDetailsForMovie("Oppenheimer").join();
    }
}
