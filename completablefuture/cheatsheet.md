**Java CompletableFuture Cheatsheet**

**Erstellung**

* `CompletableFuture<T> completableFuture = new CompletableFuture<>();`
* `CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(() -> { ... });`
* `CompletableFuture<T> completableFuture = CompletableFuture.runAsync(() -> { ... });`

**Abfrage des Status**

* `boolean isDone()`: Überprüft, ob die Aufgabe abgeschlossen ist.
* `boolean isCompletedExceptionally()`: Überprüft, ob die Aufgabe mit einer Ausnahme abgeschlossen wurde.
* `T get()`: Blockiert, bis die Aufgabe abgeschlossen ist, und gibt dann das Ergebnis zurück.
* `T get(long timeout, TimeUnit unit)`: Blockiert bis zur angegebenen Zeit, oder bis die Aufgabe abgeschlossen ist, und gibt dann das Ergebnis zurück.
* `CompletableFuture<T> join()`: Blockiert, bis die Aufgabe abgeschlossen ist, und gibt dann die Aufgabe zurück.

**Aufgaben kombinieren**

* `thenApply(Function<T, U> fn)`: Führt eine Funktion auf dem Ergebnis der Aufgabe aus und gibt das Ergebnis der Funktion zurück.
* `thenAccept(Consumer<T> consumer)`: Führt eine Aktion auf dem Ergebnis der Aufgabe aus.
* `thenRun(Runnable action)`: Führt eine Aktion aus, unabhängig vom Ergebnis der Aufgabe.

* `allOf(CompletableFuture<?>... futures)`: Liefert eine Aufgabe, die abgeschlossen ist, wenn alle angegebenen Aufgaben abgeschlossen sind.
* `anyOf(CompletableFuture<?>... futures)`: Liefert eine Aufgabe, die abgeschlossen ist, wenn eine der angegebenen Aufgaben abgeschlossen ist.
* `applyToEither(CompletableFuture<T> f1, CompletableFuture<T> f2, Function<T, U> fn)`: Führt eine Funktion auf dem Ergebnis einer der beiden Aufgaben aus, je nachdem, welche Aufgabe zuerst abgeschlossen ist.
* `acceptEither(CompletableFuture<T> f1, CompletableFuture<T> f2, Consumer<T> consumer)`: Führt eine Aktion auf dem Ergebnis einer der beiden Aufgaben aus, je nachdem, welche Aufgabe zuerst abgeschlossen ist.

**Ausnahmen behandeln**

* `exceptionally(Function<Throwable, T> fn)`: Führt eine Funktion auf einer Ausnahme aus, die bei der Ausführung der Aufgabe auftreten kann.
* `handle(BiFunction<T, Throwable, U> fn)`: Führt eine Funktion auf dem Ergebnis der Aufgabe aus oder auf einer Ausnahme, die bei der Ausführung der Aufgabe auftreten kann.

**Weitere Methoden**

* `complete(T value)`: Beendet die Aufgabe mit dem angegebenen Wert.
* `completeExceptionally(Throwable ex)`: Beendet die Aufgabe mit der angegebenen Ausnahme.
* `cancel(boolean mayInterruptIfRunning)`: Bricht die Aufgabe ab.
* `isCancelled()`: Überprüft, ob die Aufgabe abgebrochen wurde.

**Beispiele**

```java
// Erstellen einer Aufgabe, die ein Integer zurückgibt
CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
    return 10;
});

// Abfrage des Status der Aufgabe
System.out.println(completableFuture.isDone()); // false

// Abrufen des Ergebnisses der Aufgabe
completableFuture.get(); // 10

// Kombinieren von Aufgaben
CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> "World");

CompletableFuture<String> combinedFuture = completableFuture1.thenApply(s -> s + " " + completableFuture2.join());

System.out.println(combinedFuture.get()); // Hello World
```

**Weitere Informationen**

Weitere Informationen zu Java CompletableFuture finden Sie in der JavaDoc: [https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html).