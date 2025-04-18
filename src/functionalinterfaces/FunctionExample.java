package functionalinterfaces;

import java.util.function.Function;

public class FunctionExample {
    public static void main(String[] args) {
        Function<String, Integer> lengthCalculator = str -> str.length();
        System.out.println("Length: " + lengthCalculator.apply("Streams"));
    }
}