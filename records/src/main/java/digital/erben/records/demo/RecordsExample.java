package digital.erben.records.demo;

sealed interface Shape  // Sealed interfaces: Es wird aufgelistet, welche Implementierungen erlaubt sind
    permits Circle, Rectangle, Triangle {
    double area();
}

record Circle(double radius) implements Shape { // Records: Klassen, für die automatisch final-Felder, getter, equals, hashCode und toString generiert wird
    @Override
    public double area() {
        return Math.PI * Math.pow(radius(), 2);
    }
}

record Rectangle(double width, double height) implements Shape {
    @Override
    public double area() {
        return width() * height();
    }
}

record Triangle(double base, double height) implements Shape {
    @Override
    public double area() {
        return (base() * height()) / 2;
    }
}

class Main {

    public static void main(String[] args) {
        Shape circle = new Circle(3.0);
        Shape rectangle = new Rectangle(4.0, 2.0);
        Shape triangle = new Triangle(6.0, 4.0);
        printShapeArea(circle);
        printShapeArea(rectangle);
        printShapeArea(triangle);
    }

    public static void printShapeArea(Shape shape) {
        if (shape instanceof Circle circle) { // Instanceof mit Variable
            circle.area();
        }
        String shapeInfo =
            switch (shape) { // Switch Expression: Es gibt einen Rückgabewert des Switch
                case Circle c -> "Circle with radius " + c.radius(); // Bei Switch Expressions muss ein -> statt : benutzt werden
                case Rectangle r -> "Rectangle with width " +
                r.width() +
                " and height " +
                r.height();
                case Triangle t -> "Triangle with base " +
                t.base() +
                " and height " +
                t.height();
                // Kein default nötig, weil das Interface sealed ist
            };
        System.out.println(shapeInfo + " has an area of: " + shape.area());
    }
}
