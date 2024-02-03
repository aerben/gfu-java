package digital.erben.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Cart(List<LineItem> lineItems, String id) {
    @JsonCreator
    public Cart(
        @JsonProperty("lineItems") List<LineItem> lineItems,
        @JsonProperty("id") String id
    ) {
        this.lineItems = lineItems;
        this.id = id;
    }

    public Cart withId(String cartId) {
        return new Cart(this.lineItems, cartId);
    }
}
