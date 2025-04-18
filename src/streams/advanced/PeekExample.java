package streams.advanced;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * peek allows you to perform intermediate debugging or inspection during stream operations.
 */
public class PeekExample {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("apple", "banana", "mango");

        List<String> upperFruits = fruits.stream()
                                         .peek(fruit -> System.out.println("Before map: " + fruit))
                                         .map(String::toUpperCase)
                                         .peek(fruit -> System.out.println("After map: " + fruit))
                                         .collect(Collectors.toList());

        System.out.println(upperFruits); // [APPLE, BANANA, MANGO]
    }
}
