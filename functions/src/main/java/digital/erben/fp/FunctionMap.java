package digital.erben.fp;

import digital.erben.fp.exception.InvalidFunctionNameException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * {@link FunctionMap} is an API that allows you to store and retrieve functions by string name. {@link FunctionMap}
 * is stored in a {@link HashMap}, where the key is a function name, and the value is a {@link Function} instance.
 *
 */
public class FunctionMap<T, R> {

    private Map<String, Function<T, R>> functionMap;

    FunctionMap() {
        functionMap = new HashMap<>();
    }

    public void addFunction(String name, Function<T, R> function) {
        functionMap.put(name, function);
    }

    public Optional<Function<T, R>> getFunction(String name) {
        return Optional.ofNullable(functionMap.get(name));
    }
}
