package streams;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates terminal operations like forEach, count, collect, etc.
 */
public class StreamTerminalOperations {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("apple", "banana", "orange", "apple");

        long count = fruits.stream()
                .distinct()
                .count();

        System.out.println("Unique fruits count: " + count);
    }
}
