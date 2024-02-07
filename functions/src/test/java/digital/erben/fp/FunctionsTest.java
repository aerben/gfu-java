package digital.erben.fp;

import java.util.function.Function;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FunctionsTest {

    private FunctionMap<Integer, Integer> integerFunctionMap;

    @BeforeEach
    public void init() {
        integerFunctionMap = Functions.intFunctionMap();
    }

    @Test
    @Order(7)
    void squareFunction() {
        Function<Integer, Integer> squareFunction = integerFunctionMap
            .getFunction("square")
            .orElse(Function.identity());

        int result = squareFunction.apply(5);

        Assertions.assertEquals(25, result);
    }

    @Test
    @Order(1)
    void absFunction() {
        Function<Integer, Integer> absFunction = integerFunctionMap
            .getFunction("abs")
            .orElse(Function.identity());
        int result = absFunction.apply(-192);

        Assertions.assertEquals(192, result);
    }

    @Test
    @Order(5)
    void incrementFunction() {
        Function<Integer, Integer> incrementFunction = integerFunctionMap
            .getFunction("increment")
            .orElse(Function.identity());
        int result = incrementFunction.apply(399);

        Assertions.assertEquals(400, result);
    }

    @Test
    @Order(6)
    void destDecrementFunction() {
        Function<Integer, Integer> decrementFunction = integerFunctionMap
            .getFunction("decrement")
            .orElse(Function.identity());
        int result = decrementFunction.apply(800);

        Assertions.assertEquals(799, result);
    }

    @Test
    @Order(2)
    void signFunctionOnNegativeValue() {
        Function<Integer, Integer> signFunction = integerFunctionMap
            .getFunction("sgn")
            .orElse(Function.identity());
        int result = signFunction.apply(-123);

        Assertions.assertEquals(-1, result);
    }

    @Test
    @Order(3)
    void signFunctionOnPositiveValue() {
        Function<Integer, Integer> signFunction = integerFunctionMap
            .getFunction("sgn")
            .orElse(Function.identity());
        int result = signFunction.apply(23);

        Assertions.assertEquals(1, result);
    }

    @Test
    @Order(4)
    void signFunctionOnZero() {
        Function<Integer, Integer> signFunction = integerFunctionMap
            .getFunction("sgn")
            .orElse(Function.identity());
        int result = signFunction.apply(0);

        Assertions.assertEquals(0, result);
    }
}
