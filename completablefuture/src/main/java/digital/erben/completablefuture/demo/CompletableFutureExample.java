package digital.erben.completablefuture.demo;

import digital.erben.completablefuture.MovieDetailsApi;
import digital.erben.completablefuture.MovieResult;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * Beispiele für die Verwendung von CompletableFuture in Java.
 */
public final class CompletableFutureExample {

    private static final MovieRatingsDataset DATASET = MovieRatingsDataset.getInstance();
    private static final MovieDetailsApi API = MovieDetailsApi.instance();

    public static void main(String[] args) {
        // Erstmal ein einfaches Beispiel zum Warmwerden
        CompletableFuture
            .supplyAsync(() -> {
                // Some long-running operation
                return "done";
            })
            .thenAccept(System.out::println);
        // da wir nicht join oder get aufrufen, wird diese Operation
        // im Hintergrund laufen. Wird das Programm beendet bevor die Operation abgeschlossen ist, dann... pech!

        CompletableFuture
            .runAsync(() -> {
                // Dies ist ein Runnable. Es gibt nichts zurück, und das CompletableFuture ist daher ein CompletableFuture<Void>
            })
            .join(); // mit join erwarten wir das Ergebnis

        CompletableFuture.completedFuture("schon fertig"); //Falls wir eines brauchen, aber das Ergebnis schon da ist
        CompletableFuture.failedFuture(new RuntimeException((""))); // Falls wir schon wissen, dass es fehlgeschlagen ist

        CompletableFuture
            .supplyAsync(() -> "test")
            .thenApply(String::toUpperCase)
            .thenApplyAsync(String::toUpperCase)
            .thenApplyAsync(String::toUpperCase, ForkJoinPool.commonPool())
            .thenApplyAsync(String::toUpperCase, Executors.newSingleThreadExecutor())
            .thenApplyAsync(String::toUpperCase, Executors.newFixedThreadPool(12))
            .thenAccept(System.out::println);
        // Der Unterschied ist subtil, aber sehr wichtig.
        // thenApply läuft auf dem Thread, auf dem der vorherige Schritt abgelaufen ist.
        // Das kann ein beliebiger Thread sein, ggf. sogar der, auf dem wir uns gerade befinden!
        // thenApplyAsync läuft auf einem Thread, der vom Standard-Executor für CompletableFuture kommt,
        // i.d.R. ForkJoinPool.commonPool
        // man kann den Pool auch explizit definieren

        // exceptionally dient dazu, einen Fehler zu fangen und "weiterzumachen"
        CompletableFuture
            .failedFuture(new RuntimeException("foo"))
            .exceptionally(error -> {
                System.err.println(error.getMessage());
                return "ok";
            });

        // whenComplete erhält entweder den Wert der letzten Stage, oder den Fehler.
        // es ändert den Wert oder Fehler aber nicht und fungiert daher wie ein T-Stück
        CompletableFuture
            .completedFuture("")
            .whenComplete((val, err) -> {
                if (val != null) {
                    System.out.println(val);
                } else if (err != null) {
                    System.err.println(err.getMessage());
                }
            });

        // Handle wird ausgeführt sowohl im Erfolgs- und im Fehlerfall.
        // Es wird erwartet, dass der Fehler behandelt wird, wenn er auftritt.
        CompletableFuture
            .completedFuture("")
            .handle((val, err) -> {
                if (val != null) {
                    System.out.println(val);
                } else if (err != null) {
                    System.err.println(err.getMessage());
                }
                return "";
            });

        // Hier werden zwei Einträge aus dem Dataset geladen, der Name entnommen,
        // und schließlich die MoviesApi aufgerufen.
        // Die Api gibt für jeden Titel mehrere Treffer zurück
        // Da der Java HttpClient ein CompletableFuture zurückgibt, werden wir damit weiterarbeiten.
        List<CompletableFuture<List<MovieResult>>> ft = DATASET
            .load()
            .limit(2)
            .map(MovieRating::name)
            .map(API::loadDetailsForMovie)
            .toList();

        // Wir wollen zuerst waren, bis alle Ergebnisse für alle API-Aufrufe da sind
        CompletableFuture
            .allOf(ft.toArray(CompletableFuture[]::new))
            // Leider gibt es keine elegante Möglichkeit, das Ergebnis aller CompletableFutures direkt zu nutzen.
            // man muss sich behelfen, in dem man die ursprüngliche Liste der Futures oben nimmt
            // und mit join das Ergebnis extrahiert.
            // Wir müssen danach aus dem Stream<List<Result>> ein Stream<Result> machen mittels flatMap
            .thenApply(ignored ->
                ft
                    .stream()
                    .map(CompletableFuture::join)
                    .flatMap(Collection::stream)
                    .toList()
            )
            // und endlich können wir die Ergebnisse rendern
            .thenAccept(AsciiTableRenderer::render)
            .join();
    }
}
