Ein Cheatsheet für funktionale Interfaces in Java 8 enthält Informationen über die am häufigsten verwendeten funktionalen Interfaces, die mit Java 8 eingeführt wurden. Diese Interfaces sind ein zentraler Bestandteil der Lambda-Ausdrücke und der funktionalen Programmierung in Java. Sie ermöglichen es, Code auf eine konzise und flexible Weise auszudrücken. Hier sind die wichtigsten funktionalen Interfaces, die du kennen solltest:

1. **`Function<T,R>`**
    - **Beschreibung**: Repräsentiert eine Funktion, die ein Argument vom Typ `T` nimmt und ein Ergebnis vom Typ `R` zurückgibt.
    - **Methode**: `R apply(T t)`

2. **`BiFunction<T,U,R>`**
    - **Beschreibung**: Repräsentiert eine Funktion, die zwei Argumente nimmt, eines vom Typ `T` und eines vom Typ `U`, und ein Ergebnis vom Typ `R` zurückgibt.
    - **Methode**: `R apply(T t, U u)`

3. **`Predicate<T>`**
    - **Beschreibung**: Repräsentiert ein Prädikat (boolesche Funktion) eines Arguments.
    - **Methode**: `boolean test(T t)`

4. **`BiPredicate<T,U>`**
    - **Beschreibung**: Repräsentiert ein Prädikat (boolesche Funktion) von zwei Argumenten.
    - **Methode**: `boolean test(T t, U u)`

5. **`Consumer<T>`**
    - **Beschreibung**: Repräsentiert eine Operation, die ein einzelnes Input-Argument konsumiert und kein Ergebnis zurückgibt.
    - **Methode**: `void accept(T t)`

6. **`BiConsumer<T,U>`**
    - **Beschreibung**: Repräsentiert eine Operation, die zwei Input-Argumente konsumiert und kein Ergebnis zurückgibt.
    - **Methode**: `void accept(T t, U u)`

7. **`Supplier<T>`**
    - **Beschreibung**: Repräsentiert einen Lieferanten von Ergebnissen.
    - **Methode**: `T get()`

8. **`UnaryOperator<T>`**
    - **Beschreibung**: Repräsentiert eine Operation auf einem einzelnen Operanden, der denselben Typ wie das Ergebnis hat.
    - **Methode**: `T apply(T t)`

9. **`BinaryOperator<T>`**
    - **Beschreibung**: Repräsentiert eine Operation, die zwei Operanden desselben Typs nimmt und ein Ergebnis desselben Typs produziert.
    - **Methode**: `T apply(T t, T u)`

Diese funktionalen Interfaces sind Teil des `java.util.function` Pakets und spielen eine Schlüsselrolle bei der Verwendung von Lambda-Ausdrücken und Methodenreferenzen, indem sie eine zieltypisierte Weise bieten, diese Konstrukte auszudrücken.