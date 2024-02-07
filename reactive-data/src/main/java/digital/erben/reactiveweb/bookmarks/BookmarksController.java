package digital.erben.reactiveweb.bookmarks;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BookmarksController {

    private final BookmarkRepository bookmarkRepository;

    public BookmarksController(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @PostMapping("/bookmarks")
    public Mono<Bookmark> createBookmark(@RequestBody Bookmark bookmark) {
        return this.bookmarkRepository.save(bookmark);
    }

    @GetMapping("/bookmarks/{id}")
    public Mono<Bookmark> getBookmark(@PathVariable Long id) {
        return this.bookmarkRepository.findById(id);
    }

    @GetMapping("/bookmarks")
    public Flux<Bookmark> getBookmarks() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @DeleteMapping("/bookmark/{id}")
    public Mono<Void> deleteBookmarkById(@PathVariable Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

}
