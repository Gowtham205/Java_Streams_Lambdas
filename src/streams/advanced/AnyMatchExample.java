package streams.advanced;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates the use of anyMatch in Streams.
 * anyMatch returns true if *any* element matches the given predicate.
 */
public class AnyMatchExample {
    public static void main(String[] args) {
        List<Integer> scores = Arrays.asList(45, 67, 89, 94, 55, 88);

        boolean hasHighScore = scores.stream()
                                     .anyMatch(score -> score > 90);

        System.out.println("Any score > 90? " + hasHighScore); // true
    }
}
