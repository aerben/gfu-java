package digital.erben.fp;

/**
 * An util class that provides a factory method for creating an instance of a {@link FunctionMap} filled with a list
 * of functions.
 * <p>
 * TODO: implement a method and verify it by running {@link FunctionsTest}
 */
public class Functions {

    private Functions() {}

    /**
     * A static factory method that creates an integer function map with basic functions:
     * - abs (absolute value)
     * - sgn (signum function)
     * - increment
     * - decrement
     * - square
     *
     * @return an instance of {@link FunctionMap} that contains all listed functions
     */
    public static FunctionMap<Integer, Integer> intFunctionMap() {
        FunctionMap<Integer, Integer> intFunctionMap = new FunctionMap<>();
        intFunctionMap.addFunction("abs", Math::abs);
        intFunctionMap.addFunction("sgn", x -> x > 0 ? 1 : x < 0 ? -1 : 0);
        intFunctionMap.addFunction("increment", x -> ++x);
        intFunctionMap.addFunction("decrement", x -> --x);
        intFunctionMap.addFunction("square", x -> x * x);
        return intFunctionMap;
    }
}
