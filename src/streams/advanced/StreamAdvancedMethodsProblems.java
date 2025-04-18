package streams.advanced;

import java.util.Arrays;
import java.util.List;

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

        // Problem 2: Given a list of strings, return a flat list of all characters
        List<String> words = Arrays.asList("hello", "world");
        // Use flatMap to flatten the characters into one list

        // Problem 3: Given a list of employees, check if all are in "IT" department
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "IT"),
                new Employee("Bob", "IT"),
                new Employee("Charlie", "HR")
        );
        // Use allMatch or noneMatch to validate department

        // Problem 4: Skip first 2 and limit to 3 elements from a list
        List<String> items = Arrays.asList("a", "b", "c", "d", "e", "f");
        // Use skip and limit

        // Problem 5: Print all elements and collect the upper-case versions
        List<String> fruits = Arrays.asList("apple", "banana", "mango");
        // Use peek to debug and map to convert

        // Problem 6: Use reduce to calculate sum of salaries
        List<Double> salaries = Arrays.asList(50000.0, 65000.0, 42000.0, 80000.0);
        // Use reduce

        // Problem 7: Using Stream.of()
        // Create a stream from employee names, filter names longer than 4 characters

        // Problem 8: Using Optional with findFirst()
        List<String> emails = Arrays.asList("john@yahoo.com", "alice@gmail.com", "mark@outlook.com");
        // Find the first email that ends with "gmail.com", print if present

        // Problem 9: Random stream using generate()
        // Generate a stream of 5 random numbers and print them

        // Problem 10: Arithmetic progression using iterate()
        // Start from 2, add 3 each time, get first 10 numbers

        // Problem 11: Collect names from Person list and join with comma
        List<Person> people = Arrays.asList(
                new Person("Alice", "HR"),
                new Person("Bob", "IT"),
                new Person("Carol", "IT"),
                new Person("Dan", "HR")
        );
        // Use Collectors.mapping() and joining()

        // Problem 12: Convert List<Employee> to Map<name, department>
        // Use Collectors.toMap()

        // Problem 13: Use parallelStream to filter even numbers from 1 to 10000
        // Print the count and time taken
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