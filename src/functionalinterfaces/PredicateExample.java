package functionalinterfaces;

import java.util.function.Predicate;

public class PredicateExample {
    public static void main(String[] args) {
        Predicate<String> isNotEmpty = str -> !str.isEmpty();
        Predicate<Integer> isPositive = num -> num > 0;

        System.out.println(isNotEmpty.test("Java")); 
        System.out.println(isPositive.test(-5));     
    }
}