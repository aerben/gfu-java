### Übung: Erstellen eines Pizzabestellsystems mit Sealed Interfaces, Records und Switch Expressions

**Anforderungen:**

1. Definiere ein Sealed Interface namens `Pizza` und lass es von Record-Klassen, die unterschiedliche Pizzatypen repräsentieren, implementieren. Beispiel: `Margherita`, `Pepperoni`, `Veggie`.

2. Erstelle eine Enum `Size` für die Größe der Pizza, bestehend aus Werten für `SMALL`, `MEDIUM` und `LARGE`.

3. In den Record-Klassen solltest du unterschiedliche Attribute hinzufügen, um die Unterschiede zwischen den verschiedenen Pizzatypen darzustellen.
    - Beispiel: `Margherita` ist ein einfacher Pizzatyp und benötigt nur die Größe, während `Pepperoni` zusätzlich eine Option für extra Käse haben sollte, und `Veggie` sollte weitere Attribute für Gemüse haben.

4. Erstelle eine Hauptklasse namens `Pizzeria` und implementiere eine Methode `calculatePrice(Pizza)`, die den Preis einer Pizza basierend auf ihrem Typ, ihrer Größe und ihren zusätzlichen Optionen berechnet.

5. Verwende Switch Expressions in der `calculatePrice`-Methode, um den Basispreis jeder Pizza effizient zu berechnen und je nach Größe der Pizza einen Größenmultiplikator anzupassen.

6. Teste deinen erstellten Code durch Erstellen verschiedener Pizzainstanzen und durch Hinzufügen dieser Instanzen in die `calculatePrice`-Methode. Gib die Ergebnisse auf der Konsole aus.

7. Erstelle eine Methode `placeOrder` in der `Pizzeria`, die eine Liste von Pizzas annimmt, den Preis jeder Pizza berechnet und die gesamte Bestellung sowie den Gesamtpreis ausgibt.
