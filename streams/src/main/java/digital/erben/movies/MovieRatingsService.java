package digital.erben.movies;

import java.time.Duration;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class MovieRatingsService {

    private final MovieRatingsDataset ds;

    public MovieRatingsService(MovieRatingsDataset ds) {
        this.ds = ds;
    }

    /**
     * Retrieves a stream of movie ratings with a limit.
     *
     * @param limit The maximum number of movie ratings to retrieve.
     * @return A stream of MovieRating objects representing the movie ratings.
     */
    public Stream<MovieRating> getRatings(long limit) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Retrieves a stream of movie ratings sorted by votes.
     *
     * @param ascending Determines whether the ratings should be sorted in ascending order.
     * @return A stream of MovieRating objects representing the movie ratings sorted by votes.
     */
    public Stream<MovieRating> getRatingsSortedByVotes(boolean ascending) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Retrieves a stream of movie ratings greater than or equal to the specified cutoff.
     *
     * @param cutoffInclusive The minimum rating cutoff (inclusive).
     * @return A stream of MovieRating objects representing the movie ratings greater than or equal to the cutoff.
     */
    public Stream<MovieRating> getRatingsGreaterThan(double cutoffInclusive) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Retrieves a stream of names sorted alphabetically.
     *
     * @return A stream of strings representing the names sorted alphabetically.
     */
    public Stream<String> getNamesSortedAlphabetically() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Retrieves a stream of distinct genres from the movie ratings dataset.
     *
     * @return A stream of strings representing the distinct genres.
     */
    public Stream<String> getDistinctGenres() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Retrieves a stream of movie ratings that have all the specified genres.
     *
     * @param genre A set of strings representing the genres to filter by.
     * @return A stream of MovieRating objects representing the movie ratings that have all the specified genres.
     */
    public Stream<MovieRating> getMoviesHavingAllGenres(Set<String> genre) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Returns the total vote count over all movie ratings.
     *
     * @return The total vote count over all movie ratings.
     */
    public Long getTotalVoteCountOverAllRatings() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Retrieves a stream of movie ratings sorted by duration.
     *
     * @param ascending Determines whether the ratings should be sorted in ascending order.
     * @return A stream of MovieRating objects representing the movie ratings sorted by duration.
     */
    public Stream<MovieRating> getMovieRatingsSortedByDuration(boolean ascending) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Returns the average rating of movies having the specified genre.
     *
     * @param genre The genre of movies to filter by.
     * @return The average rating of movies having the specified genre.
     */
    public Float getAverageRatingOfMoviesHavingGenre(String genre) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Finds a movie rating by its name.
     *
     * @param name The name of the movie.
     * @return An optional MovieRating object representing the rating of the movie with the specified name,
     *         or an empty optional if no movie with the given name is found.
     */
    public Optional<MovieRating> findMovieWithName(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Retrieves a stream of movie ratings with a duration less than the specified duration.
     *
     * @param duration The maximum duration of the movie ratings to retrieve.
     * @return A stream of MovieRating objects representing the movie ratings with a duration less than the specified duration.
     */
    public Stream<MovieRating> getMoviesWithDurationLessThan(Duration duration) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
