package digital.erben.reactiveweb.bookmarks;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends R2dbcRepository<Bookmark, Long> {
}
