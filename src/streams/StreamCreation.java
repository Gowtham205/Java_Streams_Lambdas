package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Shows various ways to create streams.
 */
public class StreamCreation {
    public static void main(String[] args) {
        Stream<String> streamFromValues = Stream.of("Java", "Streams", "Lambdas");
        streamFromValues.forEach(System.out::println);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        list.stream().forEach(System.out::println);
    }
}
