package digital.erben.reactiveweb.bookmarks;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Bookmark {
    @Id
    private Long id;
    private long userId;
    private long cityId;

    public Bookmark(
        Long id, long userId,
        long cityId
    ) {
        this.id = id;
        this.userId = userId;
        this.cityId = cityId;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getCityId() {
        return cityId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
            "id=" + id +
            ", userId=" + userId +
            ", cityId=" + cityId +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return id == bookmark.id && userId == bookmark.userId && cityId == bookmark.cityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, cityId);
    }
}