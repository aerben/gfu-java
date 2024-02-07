package digital.erben.reactiveweb.bookmarks;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class BookmarksController {

    @PostMapping("/bookmarks")
    public Mono<Void> submitBookmark(@RequestBody Bookmark bookmark) {
        return WebClient.create().post()
            .uri("https://gfu.requestcatcher.com/test")
            .body(BodyInserters.fromValue(bookmark))
            .retrieve()
            .toBodilessEntity()
            .mapNotNull(HttpEntity::getBody);
    }

}
