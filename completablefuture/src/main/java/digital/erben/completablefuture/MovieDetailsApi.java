package digital.erben.completablefuture;

import static com.fasterxml.jackson.annotation.JsonSetter.Value.forValueNulls;
import static com.fasterxml.jackson.annotation.Nulls.AS_EMPTY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.net.http.HttpRequest.BodyPublishers.noBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MovieDetailsApi {

    private static final MovieDetailsApi INSTANCE = new MovieDetailsApi();

    public static MovieDetailsApi instance() {
        return INSTANCE;
    }

    private static final String TITLES_API_BASE_URL =
        "https://moviesdatabase.p.rapidapi.com/titles/search/title/";

    private static final String RAPID_API_KEY =
        "1945f5b9ddmsh7107d4a97e16872p1b1b9ejsnd8413c423fad";
    private static final String RAPID_API_HOST = "moviesdatabase.p.rapidapi.com";
    private final ObjectMapper om;

    private MovieDetailsApi() {
        ObjectMapper configure = new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure.configOverride(List.class).setSetterInfo(forValueNulls(AS_EMPTY));
        this.om = configure;
    }

    /**
     * Loads movie details for the given name using the Movie Database API.
     *
     * @param name the name of the movie to load details for
     * @return a CompletableFuture that resolves to a list of MovieResult objects representing the movie details
     */
    public CompletableFuture<List<MovieResult>> loadDetailsForMovie(String name) {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            URI requestUri = URI.create(
                TITLES_API_BASE_URL +
                name.replace(" ", "%20") +
                "?exact=false&titleType=movie"
            );
            HttpRequest request = HttpRequest
                .newBuilder()
                .uri(requestUri)
                .header("X-RapidAPI-Key", RAPID_API_KEY)
                .header("X-RapidAPI-Host", RAPID_API_HOST)
                .method("GET", noBody())
                .build();
            return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToApiResponse)
                .orTimeout(30, TimeUnit.SECONDS);
        }
    }

    private List<MovieResult> mapToApiResponse(String body) {
        try {
            return om.readValue(body, ApiResponse.class).results();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
