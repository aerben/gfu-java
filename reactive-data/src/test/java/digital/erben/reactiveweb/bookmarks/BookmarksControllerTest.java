package digital.erben.reactiveweb.bookmarks;

import digital.erben.reactiveweb.ReactiveWebApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(value = BookmarksController.class)
public class BookmarksControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookmarkRepository bookmarkRepository;

    private Bookmark testBookmark;

    @BeforeEach
    void setUp() {
        testBookmark = new Bookmark(1L, 1L, 1L);
    }

    @Test
    void testCreateBookmark() {
        Mono<Bookmark> bookmarkMono = Mono.just(testBookmark);
        Mockito.when(bookmarkRepository.save(Mockito.any(Bookmark.class))).thenReturn(bookmarkMono);

        webTestClient.post().uri("/bookmarks")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(testBookmark)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Bookmark.class)
            .isEqualTo(testBookmark);
    }

    @Test
    void testGetBookmark() {
        Mockito.when(bookmarkRepository.findById(1L)).thenReturn(Mono.just(testBookmark));

        webTestClient.get().uri("/bookmarks/{id}", 1)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(testBookmark.getId());
    }

    @Test
    void testDeleteBookmark() {

    }


    @Test
    void testGetAllBookmarks() {

    }

}