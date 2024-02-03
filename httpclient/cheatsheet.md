# Java 11 HttpClient Cheat Sheet

## 1. HttpClient erstellen

```java
import java.net.http.*;
import java.time.*;

var httpClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_1_1) // oder: .HTTP_2. Optional!
    .connectTimeout(Duration.ofSeconds(10)) // optional, aber sehr empfehlenswert
    .followRedirects(HttpClient.Redirect.NORMAL) // oder: .NEVER, .ALWAYS. Optional!
    .authenticator(Authenticator.getDefault()) // optional
    .build();
```

## 2. HttpRequest erstellen

### 2.1. GET-Request

```java
import java.net.URI;

var request = HttpRequest.newBuilder()
    .uri(URI.create("https://example.com"))
    .GET() // standardmäßig GET
    .header("Accept", "application/json")
    .timeout(Duration.ofSeconds(10))
    .build();
```

### 2.2. POST-Request mit JSON-Body

```java
import java.net.URI;
import java.nio.charset.StandardCharsets;

var jsonBody = "{ \"key\": \"value\" }";
var request = HttpRequest.newBuilder()
    .uri(URI.create("https://example.com"))
    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
    .header("Content-Type", "application/json")
    .header("Accept", "application/json")
    .build();
```

## 3. HttpResponse abrufen

```java
import java.util.concurrent.*;

var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .get(timeoutDuration.toSeconds(), TimeUnit.SECONDS);

int statusCode = response.statusCode();
String responseBody = response.body();
HttpHeaders headers = response.headers();
```

## 4. Synchronous vs. Asynchronous

### 4.1. Synchronous

```java
try {
    var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    // Verarbeite response
} catch (IOException | InterruptedException e) {
    // Behandle Exception
}
```

### 4.2. Asynchronous

```java
httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println)
    .exceptionally(e -> {
        // Behandle Exception
        return null;
    });
```

## 5. Benutzerdefinierte BodyHandler

```java
import java.io.*;
import java.nio.file.*;
import java.nio.file.StandardOpenOption.*;

HttpResponse.BodySubscriber<Path> bodyHandler = HttpResponse.BodySubscribers.ofFile(Paths.get("example.file"));

httpClient.sendAsync(request, bodyHandler)
    .thenApply(response -> {
        try {
            return Files.readString(response.body(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    })
    .thenAccept(System.out::println);
```