package streams.advanced;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Java 8 Streams - Cheat Sheet with Examples
 * Covers: map, filter, collect, groupBy, partitioningBy, reduce, summaryStatistics, flatMap, etc.
 */
public class StreamMethodsCheatSheet {
    public static void main(String[] args) {

        // 1. map()
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> lengths = names.stream()
                .map(String::length)
                .toList();
        System.out.println("Name lengths: " + lengths);

        // 2. filter()
        List<String> filteredNames = names.stream()
                .filter(n -> n.length() > 3)
                .toList();
        System.out.println("Names with >3 letters: " + filteredNames);

        // 3. collect() to Set
        List<Integer> nums = Arrays.asList(1, 2, 2, 3, 4, 4, 5);
        Set<Integer> evens = nums.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toSet());
        System.out.println("Even numbers as set: " + evens);

        // 4. groupingBy()
        List<Person> people = Arrays.asList(
                new Person("Alice", "HR"),
                new Person("Bob", "IT"),
                new Person("Carol", "IT"),
                new Person("Dan", "HR")
        );
        Map<String, List<Person>> groupedByDept = people.stream()
                .collect(Collectors.groupingBy(Person::getDept));
        System.out.println("Grouped by department: " + groupedByDept);

        // 5. partitioningBy()
        Map<Boolean, List<Integer>> partitioned = nums.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("Partitioned evens/odds: " + partitioned);

        // 6. summaryStatistics()
        List<Integer> ages = Arrays.asList(23, 35, 42, 29, 30);
        IntSummaryStatistics stats = ages.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        System.out.println("Average age: " + stats.getAverage());
        System.out.println("Max age: " + stats.getMax());

        // 7. distinct() and sorted()
        List<String> rawNames = Arrays.asList("John", "Jane", "John", "Alice");
        List<String> sortedUnique = rawNames.stream()
                .distinct()
                .sorted()
                .toList();
        System.out.println("Sorted unique names: " + sortedUnique);

        // 8. findFirst() and findAny()
        Optional<String> found = names.stream()
                .filter(n -> n.startsWith("C"))
                .findFirst();
        found.ifPresent(n -> System.out.println("First starting with C: " + n));

        // 9. flatMap()
        List<List<String>> nested = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("c", "d")
        );
        List<String> flattened = nested.stream()
                .flatMap(List::stream)
                .toList();
        System.out.println("Flattened list: " + flattened);

        // 10. reduce()
        int total = nums.stream()
                .reduce(0, Integer::sum);
        System.out.println("Sum using reduce: " + total);
    }
}

class Person {
    private final String name;
    private final String dept;

    public Person(String name, String dept) {
        this.name = name;
        this.dept = dept;
    }

    public String getName() { return name; }
    public String getDept() { return dept; }

    @Override
    public String toString() {
        return name + " (" + dept + ")";
    }
}