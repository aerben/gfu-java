package digital.erben.reactiveweb.bookmarks;

import digital.erben.reactiveweb.bookmarks.Bookmark;
import digital.erben.reactiveweb.bookmarks.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import java.util.List;

@DataR2dbcTest
public class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        var statements = List.of(
            "DROP TABLE IF EXISTS bookmark;",
            "CREATE TABLE IF NOT EXISTS BOOKMARK (\n" +
                "    ID LONG NOT NULL AUTO_INCREMENT,\n" +
                "    USER_ID LONG,\n" +
                "    CITY_ID LONG,\n" +
                "    PRIMARY KEY (ID)\n" +
                ");\n"
        );

        statements.forEach(it -> databaseClient.sql(it)
                .fetch()
                .rowsUpdated()
                .block());
    }

    @Test
    public void testSave() {
        Bookmark bookmark = new Bookmark(null, 1L, 1L);
        bookmarkRepository.save(bookmark)
                .as(StepVerifier::create)
                .expectNextMatches(savedBookmark -> savedBookmark.getId() == 1L)
                .verifyComplete();
    }

    @Test
    public void testFindById() {
        Bookmark bookmark = new Bookmark(null, 1L, 1L);
        bookmarkRepository.save(bookmark).block();

        bookmarkRepository.findById(1L)
                .as(StepVerifier::create)
                .expectNextMatches(foundBookmark -> foundBookmark.getId() == 1L && foundBookmark.getUserId() == 1L && foundBookmark.getCityId() == 1L)
                .verifyComplete();
    }

    @Test
    public void testDeleteById() {
        // Create a new Bookmark with userId and cityId
        Bookmark bookmark = new Bookmark(null, 1L, 1L);

        // Save the bookmark, then delete it, and finally attempt to find it to confirm deletion
        bookmarkRepository.save(bookmark)
            .flatMap(savedBookmark -> bookmarkRepository.deleteById(savedBookmark.getId()))
            .thenMany(bookmarkRepository.findById(1L))
            .as(StepVerifier::create)
            .verifyComplete(); // Verify that no Bookmark is emitted, indicating it was deleted
    }
}
