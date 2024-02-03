package digital.erben.completablefuture;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.mockito.Mockito;

public class Mocks {

    public static MovieDetailsApi movieDetailsApiMock() {
        MovieDetailsApi mock = Mockito.mock(MovieDetailsApi.class);

        MovieResult spiderManMovie = new MovieResult(
            new PrimaryImage(
                502,
                892,
                "https://i.pinimg.com/736x/ee/73/c8/ee73c81b1ec4827e01301cbb67e9c6a2.jpg"
            ),
            new TitleText("Spider-Man: Across the Spider-Verse"),
            new ReleaseDate(30, 5, 2023)
        );

        MovieResult barbieMovieResult = new MovieResult(
            new PrimaryImage(
                220,
                323,
                "https://upload.wikimedia.org/wikipedia/en/0/0b/Barbie_2023_poster.jpg"
            ),
            new TitleText("Barbie"),
            new ReleaseDate(9, 7, 2023)
        );

        Mockito
            .when(mock.loadDetailsForMovie(Mockito.startsWith("Spider-Man")))
            .thenReturn(CompletableFuture.completedFuture(List.of(spiderManMovie)));

        Mockito
            .when(mock.loadDetailsForMovie(Mockito.eq("Oppenheimer")))
            .thenReturn(
                CompletableFuture
                    .<List<MovieResult>>supplyAsync(() -> {
                        try {
                            Thread.sleep(Duration.ofMinutes(1));
                            throw new RuntimeException();
                        } catch (InterruptedException ignored) {
                            throw new RuntimeException();
                        }
                    })
                    .orTimeout(3, TimeUnit.SECONDS)
            );

        Mockito
            .when(mock.loadDetailsForMovie(Mockito.eq("Barbie")))
            .thenReturn(CompletableFuture.completedFuture(List.of(barbieMovieResult)));

        Mockito
            .when(mock.loadDetailsForMovie(Mockito.isNull()))
            .thenReturn(
                CompletableFuture.failedFuture(
                    new IllegalArgumentException("Null value not allowed")
                )
            );

        return mock;
    }
}
