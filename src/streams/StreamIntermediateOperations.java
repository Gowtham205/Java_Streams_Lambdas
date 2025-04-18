package streams;

import java.util.Arrays;
import java.util.List;

public class StreamIntermediateOperations {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("John", "Mike", "Alice", "Bob");

        names.stream()
             .filter(name -> name.length() > 3)
             .map(String::toUpperCase)
             .sorted()
             .forEach(System.out::println);
    }
}