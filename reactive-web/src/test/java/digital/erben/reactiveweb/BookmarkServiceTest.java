package digital.erben.reactiveweb;

import digital.erben.reactiveweb.bookmarks.Bookmark;
import digital.erben.reactiveweb.bookmarks.BookmarkService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

public class BookmarkServiceTest {
    @Test
    void shouldSendSync() {
        new BookmarkService(RestClient.create(), WebClient.create()).createBookmark(new Bookmark(1l, 2l));
    }

    @Test
    void shouldSendASync() throws InterruptedException {
        new BookmarkService(RestClient.create(), WebClient.create()).createBookmarkAsync(new Bookmark(1l, 2l)).subscribe(System.out::println);
        Thread.sleep(10000L);
    }
}
