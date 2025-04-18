package streams.advanced;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstrates flatMap, used to flatten nested structures (like List of Lists).
 */
public class FlatMapExample {
    public static void main(String[] args) {
        List<List<String>> nestedWords = Arrays.asList(
            Arrays.asList("hello", "world"),
            Arrays.asList("java", "stream")
        );

        List<String> flatList = nestedWords.stream()
                                           .flatMap(List::stream)
                                           .collect(Collectors.toList());

        System.out.println(flatList); // [hello, world, java, stream]
    }
}
