package digital.erben.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LineItem(String productId, String productName, int quantity) {
    @JsonCreator
    public LineItem(
        @JsonProperty("productId") String productId,
        @JsonProperty("productName") String productName,
        @JsonProperty("quantity") int quantity
    ) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }
}
