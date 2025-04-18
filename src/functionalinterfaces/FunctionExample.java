package functionalinterfaces;

import java.util.function.Function;

/**
 * Demonstrates Function functional interface which accepts one argument and returns a result.
 */
public class FunctionExample {
    public static void main(String[] args) {
        Function<String, Integer> lengthCalculator = str -> str.length();
        System.out.println("Length: " + lengthCalculator.apply("Streams"));
    }
}
