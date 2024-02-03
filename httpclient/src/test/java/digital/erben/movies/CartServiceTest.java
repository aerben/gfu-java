package digital.erben.movies;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.Cookie.cookie;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.CREATED_201;
import static org.mockserver.model.Parameter.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;

@ExtendWith(MockServerExtension.class)
class CartServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockServerClient client;

    @BeforeEach
    public void beforeEach(MockServerClient client) {
        this.client = client;
    }

    @Test
    void shouldRetrieveCart() throws JsonProcessingException {
        var sessionId = randomUUID();

        var randomCart = createRandomCart();
        var body = objectMapper.writeValueAsString(randomCart);
        client
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/view/cart")
                    .withCookie(cookie("sessionId", ".*" + sessionId + ".*"))
                    .withQueryStringParameters(param("cartId", randomCart.id()))
            )
            .respond(response().withBody(body));
        var res = new CartService("localhost", client.getPort())
            .retrieveCartWithId(UUID.fromString(randomCart.id()), sessionId);

        assertTrue(res.isPresent());
        assertEquals(randomCart, res.get());
    }

    private static Cart createRandomCart() {
        var cartId = randomUUID();
        return new Cart(
            List.of(
                new LineItem(randomUUID().toString(), Faker.instance().beer().name(), 6)
            ),
            cartId.toString()
        );
    }

    @Test
    void shouldCreateCart() throws JsonProcessingException {
        var sessionId = randomUUID();

        var cartForCreation = createRandomCart().withId(null);
        var bodyBeforeCreation = objectMapper.writeValueAsString(cartForCreation);

        var cartId = randomUUID();
        var cartAfterCreation = cartForCreation.withId(cartId.toString());
        var bodyAfterCreation = objectMapper.writeValueAsString(cartAfterCreation);

        client
            .when(
                request()
                    .withMethod("POST")
                    .withPath("/view/cart")
                    .withCookie(cookie("sessionId", ".*" + sessionId + ".*"))
                    .withBody(bodyBeforeCreation)
            )
            .respond(
                response().withStatusCode(CREATED_201.code()).withBody(bodyAfterCreation)
            );
        var res = new CartService("localhost", client.getPort())
            .create(cartForCreation, sessionId);
        assertEquals(cartAfterCreation, res);
    }

    @Test
    void shouldReturnEmptyOptionalWhenNotFound() {
        client
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/view/cart")
                    .withCookie(cookie("sessionId", ".*"))
                    .withQueryStringParameters(param("cartId", ".*"))
            )
            .respond(response().withStatusCode(404));
        var res = new CartService("localhost", client.getPort())
            .retrieveCartWithId(randomUUID(), randomUUID());
        assertTrue(res.isEmpty());
    }

    @Test
    void shouldDeleteCart() throws JsonProcessingException {
        var sessionId = randomUUID();

        Cart cart = createRandomCart();
        var body = objectMapper.writeValueAsString(cart);

        client
            .when(
                request()
                    .withMethod("DELETE")
                    .withPath("/view/cart")
                    .withCookie(cookie("sessionId", ".*" + sessionId + ".*"))
                    .withQueryStringParameters(param("cartId", cart.id()))
            )
            .respond(response().withStatusCode(200).withBody(body));
        new CartService("localhost", client.getPort())
            .delete(UUID.fromString(cart.id()), sessionId);
    }
}
