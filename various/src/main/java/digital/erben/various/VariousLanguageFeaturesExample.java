package digital.erben.various;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class VariousLanguageFeaturesExample {

    static class GenericRecord<T extends Number> {
        private final T number;

        GenericRecord(T number) {
            this.number = number;
        }

        T getNumber() {
            return number;
        }
    }

    public static void main(String[] args) {
        List.of("1", "2", "3"); // immutable!
        Set.of("1", "2");

        GenericRecord<?> r = new GenericRecord<>(2d) {
            //war vorher verboten: Typ musste explizit angegeben werden
        };
        GenericRecord<? extends Number> r2 = new GenericRecord<>(2d){
            // ebenso
        };

        ProcessHandle self = ProcessHandle.current();
        long PID = self.pid();
        System.out.println(PID);

        ProcessHandle.Info procInfo = self.info();
        procInfo.arguments().map(Arrays::toString).ifPresent(System.out::println);
        procInfo.commandLine().ifPresent(System.out::println);
        procInfo.startInstant().ifPresent(System.out::println);
        procInfo.totalCpuDuration().ifPresent(System.out::println);
    }
}
