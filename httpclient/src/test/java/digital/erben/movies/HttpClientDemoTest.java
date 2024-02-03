package digital.erben.movies;

import com.github.javafaker.Faker;
import java.awt.*;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.base.Charsets;

/**
 * Diese Klasse enthält einige Beispiele dafür, wie man die HttpClient-API in Java verwendet.
 */
@Testcontainers
public class HttpClientDemoTest {

    @Container
    public static GenericContainer<?> httpbin = new GenericContainer<>(
        "kennethreitz/httpbin:latest"
    )
        .withExposedPorts(80);

    @Test
    void performSimpleGet() throws IOException, InterruptedException {
        // HttpClient ist Autocloseable. Wir sollten ihn also in einem try-with-resources-Block benutzen.
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            // Die Java 11 HttpClient-API macht heftigen Gebrauch von statischen Factory-Methoden.
            // Das kann man lieben oder hassen - der Code sieht dadurch, sobald er geschrieben ist, aber durchaus übersichtlich aus.
            HttpResponse<String> response = httpClient.send(
                HttpRequest // Die HttpRequest-Klasse enthält alle relevanten Builder. Merkt sie euch gut, denn sie ist der wichtigste Einsprungpunkt in die API.
                    // passt beim Bauen der URI auf UrlEncoding der Parameter auf. Leider bietet die
                    // API keine Möglichkeit, Parameter programmatisch hinzuzufügen und direkt zu encoden.
                    // URI.create macht dies an manchen Stellen automatisch, aber nicht alle Zeichen. Beispielsweise müsst ihr Leerzeichen selbst encoden.
                    .newBuilder(URI.create(baseurl() + "/get?myparam=myvalue"))
                    // Die Auswahl der HTTP-Methode ist denkbar einfach.
                    .GET()
                    .build(),
                // Etwas eigenwillig: Die API erwartet einen BodyHandler, der das Ergebnis des Calls verarbeitet.
                // Ihr könnt die BodyHandler durchgehen, die als statische Factory-Methoden in BodyHandlers angeboten werden.
                // Die wichtigsten sind definitiv ofString() und ofFile()
                // Auch diese Zeile merkt ihr euch besser, damit sie schnell von der Hand geht.
                HttpResponse.BodyHandlers.ofString(Charsets.UTF_8)
            );
            // Der BodyHandler, den ofString() zurückgibt, erzeugt erwartungsgemäß einen String-Response-Body.
            System.out.println(response.body());
            // Ihr könnt auch noch andere Informationen aus der Response bekommen – inklusive des Requests selbst.
            System.out.println(response.statusCode());
            System.out.println(response.headers());
        }
    }

    @Test
    void performHttpGetToFile() throws IOException, InterruptedException {
        // In diesem Beispiel laden wir ein Bild von einem Webserver in eine temporäre Datei.
        Path tempfile = Files.createTempFile("image", ".png");
        tempfile.toFile().deleteOnExit();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            httpClient.send(
                HttpRequest
                    // Es gibt hier nur zwei Neuerungen: Wir setzen einen Accept-Header
                    .newBuilder(URI.create(baseurl() + "/image"))
                    .GET()
                    .header("Accept", "image/png")
                    .build(),
                // ... und wir verwenden einen BodyHandler, der das Ergebnis in eine Datei schreibt
                HttpResponse.BodyHandlers.ofFile(tempfile)
            );
        }
        // Kanntest ihr das schon? Man kann mit der Desktop-Klasse ganz einfach eine Datei mit dem Standard-
        // Programm öffnen, welches im OS registriert ist.
        Desktop.getDesktop().open(tempfile.toFile());
    }

    @Test
    void performHttpPostAsync() throws IOException, InterruptedException {
        // In diesem Beispiel laden wir eine Datei hoch
        Path tempfile = Files.createTempFile("textfile", ".txt");
        Files.writeString(tempfile, Faker.instance().chuckNorris().fact());
        tempfile.toFile().deleteOnExit();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            httpClient
                // Es ist durchaus empfehlenswert bei HTTP-Requests nicht zu blockieren, sondern mit Hilfe von sendAsync()
                // auf die CompletableFuture-API zurückzugreifen.
                .sendAsync(
                    HttpRequest
                        .newBuilder(URI.create(baseurl() + "/post"))
                        // Bei Requests mit einem Payload wird ein BodyPublisher erwartet, der analog zum BodyHandler
                        // einen Payload erzeugt. ofFile() wird für Dateiuploads genutzt und kümmert sich auch um
                        // das Schreiben eines korrekten Multipart-Requests und
                        // das Setzen des Content-Length-Headers, allerdings nicht den Content-Type-Header.
                        .POST(HttpRequest.BodyPublishers.ofFile(tempfile))
                        .header("Content-Type", "multipart/form-data")
                        .build(),
                    HttpResponse.BodyHandlers.ofString()
                )
                // Wir erhalten ein CompletableFuture, welches wir IMMER mit einem Timeout versehen sollten.
                .orTimeout(10, TimeUnit.SECONDS)
                // Dieses können wir entweder mit get() bzw. join() erwarten oder, noch besser, mit einem Lambda
                // weiterverarbeiten. Das ist besonderes bei Requests nützlich, die tendenziell eher länger brauchen.
                // Dann wird der Thread nicht blockiert.
                .thenAccept(result -> System.out.println(result.body()))
                // Würden wir nicht join() aufrufen, würde der Test an dieser Stelle beendet werden, bevor die Antwort
                // vom Server kommt.
                .join();
        }
    }

    @Test
    void withCookie() throws IOException, InterruptedException {
        // Die Http Cookie API in Java existiert seit 1.6
        HttpCookie sessionCookie = new HttpCookie(
            "session",
            "53616c7465645f5f9d467d3ae831ec1b1e7289ef45d256224786e1ed13"
        );
        sessionCookie.setPath("/");
        CookieManager manager = new CookieManager();
        manager.getCookieStore().add(URI.create(baseurl()), sessionCookie);

        // Hier setzen wir mal einen Connect-Timeout. Auch das ist zu empfehlen.
        try (
            HttpClient httpClient = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                // hier setzen wir den CookieManager/CookieHandler
                .cookieHandler(manager)
                .build()
        ) {
            HttpResponse<String> response = httpClient.send(
                HttpRequest
                    .newBuilder(URI.create(baseurl() + "/get?myparam=myvalue"))
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString(Charsets.UTF_8)
            );
            System.out.println(response.body());
        }
    }

    @NotNull
    private static String baseurl() {
        return "http://localhost:" + httpbin.getMappedPort(80);
    }
}
