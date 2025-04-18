package streams.advanced;

import java.util.Arrays;
import java.util.List;

/**
 * reduce combines all stream elements into a single result.
 */
public class ReduceExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(10, 20, 30, 40);

        int sum = numbers.stream()
                         .reduce(0, (a, b) -> a + b); // Sum all

        System.out.println("Sum: " + sum); // 100
    }
}
