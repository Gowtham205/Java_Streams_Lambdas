package streams.advanced;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * This class contains problems to practice advanced Stream methods such as:
 * - anyMatch, allMatch, noneMatch
 * - flatMap
 * - limit, skip
 * - peek
 * - reduce
 * Solve the problems using Java 8 streams. Initial data setup is provided.
 * Practice Problems on Java Stream Advanced Methods
 * Problems: anyMatch, flatMap, allMatch, skip/limit, peek/map, reduce
 * Additional: Stream.of, Optional, generate, iterate, mapping/joining, toMap, parallelStream
 */
public class StreamAdvancedMethodsProblems {
    public static void main(String[] args) {
        // Problem 1: Check if any student scored more than 90
        List<Integer> scores = Arrays.asList(45, 67, 89, 94, 55, 88);
        // Use anyMatch to check for scores > 90

        boolean more = scores.stream().anyMatch(n -> n > 90);
        System.out.println(" scores > 90" + more);


        // Problem 2: Given a list of strings, return a flat list of all characters
        List<String> words = Arrays.asList("hello", "world");
        // Use flatMap to flatten the characters into one list
        List<String> src = words.stream().flatMap(word -> word.chars()
                        .mapToObj(c -> String.valueOf((char) c)))
                .toList();

        // Problem 3: Given a list of employees, check if all are in "IT" department
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "IT"),
                new Employee("Bob", "IT"),
                new Employee("Charlie", "HR")
        );
        // Use allMatch or noneMatch to validate department

        boolean emp = employees.stream().allMatch(dept -> dept.equals("IT"));
        System.out.println("all department:" + emp);

        // Problem 4: Skip first 2 and limit to 3 elements from a list
        List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");
        // Use skip and limit
        items
                .stream()
                .skip(2)
                .limit(3)
                .forEach(System.out::println);

        // Problem 5: Print all elements and collect the upper-case versions
        List<String> fruits = Arrays.asList("apple", "banana", "mango");
        // Use peek to debug and map to convert
        List<String> Upper = fruits.stream().map(String::toUpperCase)
                .peek(fruit -> System.out.println("After map: " + fruit))
                .collect(Collectors.toList());
        System.out.println(Upper);

        // Problem 6: Use reduce to calculate sum of salaries
        List<Double> salaries = Arrays.asList(50000.0, 65000.0, 42000.0, 80000.0);
        // Use reduce
        Double sum = salaries.stream().reduce((double) 0, (a, b) -> a + b);

        System.out.println("Sum of all sal" + sum);

        // Problem 7: Using Stream.of()
        // Create a stream from employee names, filter names longer than 4 characters
        List<String> upper = Stream.of("chandu", "gowtham", "ganesh", "UV")
                .filter(e -> e.length() > 4)
                .peek(e -> System.out.println("Filtered value: " + e)).collect(Collectors.toList());
        System.out.println(upper);

        // Problem 8: Using Optional with findFirst()
        List<String> emails = Arrays.asList("john@yahoo.com", "alice@gmail.com", "mark@outlook.com");
        // Find the first email that ends with "gmail.com", print if present
        String firstEmail = emails.stream().filter(s -> s.endsWith("gmail.com")).findFirst().orElse("Not Found any");
        System.out.println(firstEmail);

        // Problem 9: Random stream using generate()
        // Generate a stream of 5 random numbers and print them
        List<Double> randoms = Stream
                .generate(Math::random)
                .limit(5)
                .toList();
        randoms.forEach(System.out::println);

        // Problem 10: Arithmetic progression using iterate()
        // Start from 2, add 3 each time, get first 10 numbers
        /*public static List<Integer> arithmeticProgression(int start, int step, int count) {
            return Stream.iterate(start, n -> n + step)
                    .limit(count)
                    .collect(Collectors.toList());
        }*/
        List<Integer> firstTen = Stream.iterate(2, n -> n + 3)
                .limit(10)
                .toList();
        System.out.println(firstTen);

        // Alternative
        /*int[] ap = IntStream.iterate(2, n -> n + 3)
                .limit(10)
                .toArray();
        System.out.println(Arrays.toString(ap));*/

        // Problem 11: Collect names from Person list and join with comma
        List<Person> people = Arrays.asList(
                new Person("Alice", "HR"),
                new Person("Bob", "IT"),
                new Person("Carol", "IT"),
                new Person("Dan", "HR")
        );
        String resultString = people.stream().map(Person::getName).collect(Collectors.joining(","));
        System.out.println(resultString);

        // Use Collectors.mapping() and joining()

        // Problem 12: Convert List<Employee> to Map<name, department>
        // Use Collectors.toMap()
        List<Employee> employeeList = Arrays.asList(
                new Employee("Alice", "HR"),
                new Employee("Bob", "Engineering"),
                new Employee("Charlie", "Engineering"),
                new Employee("David", "HR"),
                new Employee("Eve", "Finance")
        );
        Map<String, String> empDeptMap = employeeList.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getDepartment));
        System.out.println(empDeptMap);

        // Problem 13: Use parallelStream to filter even numbers from 1 to 10000
        // Print the count and time taken
        List<Integer> nums = IntStream.rangeClosed(1, 1000)
                .boxed()
                .toList();
        long parStart = System.nanoTime();
        long parCount = nums.parallelStream()
                .filter(n -> n % 2 == 0)
                .count();
        long parTime = System.nanoTime() - parStart;
        System.out.println("=== Parallel ===");
        System.out.printf("Count : %d%n", parCount);
        System.out.printf("Time  : %.3f ms%n", parTime / 1_000_000.0);
    }
}

class Employee {
    private final String name;
    private final String department;

    public Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return name + " (" + department + ")";
    }
}