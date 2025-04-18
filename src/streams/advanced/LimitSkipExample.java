package streams.advanced;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates skip and limit which are useful for pagination and slicing.
 */
public class LimitSkipExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("a", "b", "c", "d", "e");

        names.stream()
             .skip(2)      // Skip first 2: "c", "d", "e"
             .limit(2)     // Take next 2: "c", "d"
             .forEach(System.out::println);
    }
}
