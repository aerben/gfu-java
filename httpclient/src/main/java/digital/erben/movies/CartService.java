package digital.erben.movies;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

/**
 * The CartService class provides methods to interact with a cart in a remote service.
 */
public class CartService {

    private final String host;
    private final int port;

    public CartService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Retrieves a cart with the specified cartId and sessionId.
     *
     * @param cartId    The UUID of the cart to retrieve.
     * @param sessionId The UUID of the session.
     * @return An Optional containing the retrieved Cart, or empty if the cart is not found.
     * @throws RuntimeException if there is an error processing the response body.
     */
    Optional<Cart> retrieveCartWithId(UUID cartId, UUID sessionId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Creates a new cart.
     *
     * @param cart      The Cart to be created.
     * @param sessionId The UUID of the session.
     * @return The created Cart.
     * @throws RuntimeException If there is an error processing the response body.
     */
    public Cart create(Cart cart, UUID sessionId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Deletes a cart with the specified cartId and sessionId.
     *
     * @param cartId The UUID of the cart to delete.
     * @param sessionId The UUID of the session.
     */
    public void delete(UUID cartId, UUID sessionId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Sets up a CookieManager with a session cookie for the given session ID and URI.
     * The session cookie is added to the CookieStore associated with the CookieManager.
     *
     * @param sessionId The UUID of the session.
     * @param uri       The URI for which the session cookie is being set up.
     * @return The initialized CookieManager instance.
     */
    private static CookieManager setupCookieManager(UUID sessionId, URI uri) {
        HttpCookie sessionCookie = new HttpCookie("sessionId", sessionId.toString());
        sessionCookie.setPath("/");
        CookieManager manager = new CookieManager();
        manager.getCookieStore().add(uri, sessionCookie);
        return manager;
    }

    private String baseUrl() {
        return "http://" + host + ":" + port + "/view/cart";
    }
}
