package digital.erben.reactiveweb.bookmarks;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
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
        throw new UnsupportedOperationException();
    }

    public void createBookmark(Bookmark bookmark) {
        throw new UnsupportedOperationException();
    }
}
