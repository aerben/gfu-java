package digital.erben.reactiveweb.bookmarks;

import digital.erben.reactiveweb.bookmarks.api.DefaultApi;
import digital.erben.reactiveweb.bookmarks.model.Bookmark;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class Api implements DefaultApi {

    @Override
    public Mono<ResponseEntity<Flux<Bookmark>>> bookmarksGet(ServerWebExchange exchange) {
        return DefaultApi.super.bookmarksGet(exchange);
    }

    @Override
    public Mono<ResponseEntity<Void>> bookmarksIdDelete(Long id, ServerWebExchange exchange) {
        return DefaultApi.super.bookmarksIdDelete(id, exchange);
    }

    @Override
    public Mono<ResponseEntity<Bookmark>> bookmarksIdGet(Long id, ServerWebExchange exchange) {
        return DefaultApi.super.bookmarksIdGet(id, exchange);
    }

    @Override
    public Mono<ResponseEntity<Bookmark>> bookmarksPost(Mono<Bookmark> bookmark, ServerWebExchange exchange) {
        return DefaultApi.super.bookmarksPost(bookmark, exchange);
    }
}
