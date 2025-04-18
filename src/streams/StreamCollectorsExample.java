package streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shows usage of Collectors to collect stream output.
 */
public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("java", "stream", "collect", "lambda");

        String result = words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(", "));

        System.out.println(result);
    }
}
