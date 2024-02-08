package digital.erben.reactiveweb.bookmarks;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BookmarkService {

    private final RestClient restClient;
    private final WebClient webClient;

    public BookmarkService(RestClient restClient, WebClient webClient) {
        this.restClient = restClient;
        this.webClient = webClient;
    }

    public Mono<Void> createBookmarkAsync(Bookmark bookmark) {
        return WebClient.create().post()
            .uri("https://gfu.requestcatcher.com/test")
            .body(BodyInserters.fromValue(bookmark))
            .retrieve()
            .toBodilessEntity()
            .mapNotNull(ResponseEntity::getBody);
    }

    public void createBookmark(Bookmark bookmark) {
        ResponseEntity<Void> entity = RestClient.create().post()
            .uri("https://gfu.requestcatcher.com/test")
            .body(bookmark)
            .retrieve()
            .toBodilessEntity();
        System.out.println(entity);
    }
}
