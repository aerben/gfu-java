# Java Records Cheatsheet

## Hauptfeatures
* Einfacher, prägnanter Klassencode
* Unveränderliche Daten
* Semantisch korrekte Standardimplementierungen für objektübergreifende Methoden

## Anwendung
* Datenträgerobjekte
* Wertobjekte in DDD (Domain-Driven Design)
* Einfacher Übertrag von Daten zwischen Schichten

## Erzeugen von Java Records
```java
public record Point(int x, int y) { }
```

## Automatisch generierte Methoden
* Konstruktor
* Getter-Methoden
* `equals()`
* `hashCode()`
* `toString()`

## Wichtige Merkmale und Einschränkungen
* Unveränderbar (alle Felder sind final)
* Kein Standardkonstruktor und keine Set-Methoden
* Keine Unterklassebildung oder Erweiterungen
* Implementierung von Schnittstellen ist erlaubt
* Zugriffsmodifizierer wie public und private sind anwendbar
* Verwendung von bekannten Membervariablen innerhalb der Record-Klammern

## Compact Constructor
```java
public record Point(int x, int y) {
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Both x and y must be non-negative.");
        }
    }
}
```

## Static und Instance Methoden
```java
public record Point(int x, int y) {
    public int distanceFromOrigin() {
        return (int) Math.sqrt(x * x + y * y);
    }
    
    public static Point origin() {
        return new Point(0, 0);
    }
}
```

## Vor- und Nachteile von Java Records
### Vorteile
* Reduktion von Boilerplate-Code
* Erhöhte Lesbarkeit
* Immutable von Natur aus

### Nachteile
* Einschränkungen bei Vererbung und Modifizierbarkeit
* Nicht geeignet für komplexe Datenstrukturen und Logik