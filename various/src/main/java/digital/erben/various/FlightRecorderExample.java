package digital.erben.various;

import java.util.ArrayList;
import java.util.List;

public class FlightRecorderExample {
    public static void main(String[] args) {
        List<Object> items = new ArrayList<>(1);
        try {
            while (true){
                items.add(new Object());
            }
        } catch (OutOfMemoryError e){
            System.out.println(e.getMessage());
        }
    }

}
