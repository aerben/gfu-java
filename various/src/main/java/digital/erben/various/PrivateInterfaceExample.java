package digital.erben.various;

public interface PrivateInterfaceExample {
    private static String staticPrivate() {
        return "static private";
    }

    private String instancePrivate() {
        return "instance private";
    }

    default void check() {
        System.out.println(staticPrivate());
        System.out.println(this.instancePrivate());
    }

    static void main(String[] args) {
        var impl = new PrivateInterfaceExample(){ // Typ-Inferenz: In vielen Fällen kann der Compiler den Typ einer Variable selbst ermitteln
        };
        impl.check();
    }
}