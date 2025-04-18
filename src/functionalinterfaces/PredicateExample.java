package functionalinterfaces;

import java.util.function.Predicate;

/**
 * Demonstrates Predicate functional interface which returns a boolean.
 */
public class PredicateExample {
    public static void main(String[] args) {
        Predicate<String> isNotEmpty = str -> !str.isEmpty();
        Predicate<Integer> isPositive = num -> num > 0;

        System.out.println(isNotEmpty.test("Java")); // true
        System.out.println(isPositive.test(-5));     // false
    }
}