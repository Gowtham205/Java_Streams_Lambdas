package streams.advanced;

import java.util.Arrays;
import java.util.List;

/**
 * allMatch returns true if *all* elements match the predicate.
 * noneMatch returns true if *no* elements match the predicate.
 */
public class MatchExample {
    public static void main(String[] args) {
        List<String> departments = Arrays.asList("IT", "IT", "HR");

        boolean allIT = departments.stream().allMatch(dept -> dept.equals("IT"));
        boolean noneFinance = departments.stream().noneMatch(dept -> dept.equals("Finance"));

        System.out.println("All in IT? " + allIT);           // false
        System.out.println("None in Finance? " + noneFinance); // true
    }
}
