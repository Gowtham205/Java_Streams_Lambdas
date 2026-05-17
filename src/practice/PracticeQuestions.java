package practice;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PracticeQuestions {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "HR", 45000),
                new Employee("Bob", "Engineering", 75000),
                new Employee("Charlie", "Engineering", 88000),
                new Employee("David", "HR", 39000),
                new Employee("Eve", "Finance", 61000)
        );

        // Q1: Given a List of employee names, filter only those starting with 'A',
        // convert to uppercase, and collect into a new list.
        List<Employee> employeesWithPrefixA = employees.stream()
                .filter(e -> e.getName().startsWith("A"))
                .peek(e -> e.setName(e.getName().toUpperCase()))
                .toList();
        System.out.println(employeesWithPrefixA);

        // Q2: From a List, find the sum of all even numbers using streams.
        List<Integer> nums = List.of(1, 2, 5, 6, 3, 8);
        // Answer 1
//        int sum = nums.stream().filter(n -> n % 2 == 0).mapToInt(Integer::intValue).sum();
        // Answer 2
        int sum = nums.stream().filter(n -> n % 2 == 0).reduce(0, Integer::sum);
        System.out.println(sum);

        // Q3: Given a List, count how many strings have length greater than 5.
        List<String> stringList = List.of("random", "alphabets", "mixedCase", "UPPERCASE", "none");
        long count = stringList.stream().filter(s -> s.length() > 5).count();
        System.out.println(count);

        // Q4: From a List, remove duplicates and sort alphabetically.
        List<String> strings = List.of("random", "none", "alphabets", "mixedCase", "UPPERCASE", "none");
        strings = strings.stream().distinct().sorted().toList();
        System.out.println(strings);

        // Q5: Find the maximum and minimum salary from a List where Employee has a getSalary() method.
        double maxSalary = employees.stream().mapToDouble(Employee::getSalary).max().orElse(0);
        System.out.println(maxSalary);
        double minSalary = employees.stream().mapToDouble(Employee::getSalary).min().orElse(0);
        System.out.println(minSalary);

        // Q6: Convert a List of numbers like ["1","2","3"] into a List of Integer.
        List<String> numbers = List.of("1", "2", "3");
        List<Integer> num = numbers.stream().map(Integer::parseInt).toList();
        System.out.println(num);
        List<Integer> num1 = numbers.stream().map(Integer::valueOf).toList();
        System.out.println(num1);

        // Q7: Group a List by their department name using Collectors.groupingBy().
        Map<String, List<Employee>> groupBy = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
        System.out.println(groupBy);
        Map<String, Long> groupByCount = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
        System.out.println(groupByCount);

        // Q8: From a List<List>, flatten into a single List using flatMap.
        List<List<String>> multiList = List.of(
                List.of("Apple", "Banana", "Carrot"),
                List.of("DragonFruit", "Element", "Family")
        );

        List<String> flatList = multiList.stream().flatMap(Collection::stream).toList();
        System.out.println(flatList);

        // Q9: Find the first employee with salary > 80000, or return null if none found.
        Employee firstEmployee = employees.stream().filter(e -> e.getSalary() > 60000).findFirst().orElse(null);
        System.out.println(firstEmployee);

        // Q10: Partition a List into two groups — even and odd — using a single stream operation.
        Map<Boolean, List<Integer>> result = nums.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println(result);

        // Q11: Given a List, join all elements with a comma separator and wrap with [ and ].
        String str = strings.stream().collect(Collectors.joining(",", "[", "]"));
        System.out.println(str);

        // Q12: Get the average salary of all employees whose department is 'Engineering'.
        double avgSalary = employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);
        System.out.println("Avg salary : " + avgSalary);

        // Q13: From a Map, compute the sum of all values across all keys.
        Map<Integer, Integer> keyMap = Map.of(1, 5, 2, 10, 3, 15, 4, 20, 5, 25);
        int sumOfMap = keyMap.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println(sumOfMap);

        // Q14: Sort a List first by department (ascending), then by salary (descending) within each department.
        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparing(Employee::getDepartment)
                        .thenComparing(Comparator.comparing(Employee::getSalary).reversed())
                )
                .toList();
        System.out.println(sortedEmployees);

        // Q15: Convert a List to a Map where the key is the employee ID and the value is the name
        Map<String, Double> empNameAndSalary = employees.stream()
                .collect(Collectors.toMap(Employee::getName, Employee::getSalary));
        System.out.println(empNameAndSalary);

        // Q16: Find the top 3 highest-paid employees from a List.
        List<Employee> topThreePaidEmployees = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(3)
                .toList();
        System.out.println(topThreePaidEmployees);

        // Q17: Using Collectors.toMap, build a frequency map (word → count) from a List of words.
        List<String> words = List.of("these", "are", "some", "random", "words", "taking", "for", "example", "these", "are");
        Map<String, Integer> wordCountMap = words.stream().collect(Collectors.toMap(w -> w, w -> 1, Integer::sum));
        System.out.println(wordCountMap);
        // alternative approach
        Map<String, Long> wordCountMap2 = words.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        System.out.println(wordCountMap2);

        // Q18: From a List, find the department with the highest total transaction amount.
        Optional<String> value = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.summingDouble(Employee::getSalary)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        value.ifPresent(System.out::println);

        // Q19: Check if ALL employees have a non-null email AND if ANY employee has a salary > 100000 — in a single pass if possible.
        // Here using department field as email for this problem
        List<Employee> employeesEmails = Arrays.asList(
                new Employee("Alice", "alice@co.com", 120000),
                new Employee("Bob",   "bob@co.com",    85000),
                new Employee("Carol", null,             95000),  // null email!
                new Employee("Dave",  "dave@co.com",    60000)
        );

        // Approach 1
        checkEmployees(employeesEmails);
        // All have email:    false  ← Carol has null
        // Any salary > 100k: true  ← Alice earns 120000

        // Approach 2
        checkEmployeesSinglePass(employeesEmails);
        // Same output, single iteration

        // Approach 3
        checkWithReduce(employeesEmails);
    }

    public static void checkEmployees(List<Employee> employees) {
        boolean allHaveEmail  = employees.stream()
                .allMatch(e -> e.getDepartment() != null);

        boolean anyHighSalary = employees.stream()
                .anyMatch(e -> e.getSalary() > 100000);

        System.out.println("All have email:   " + allHaveEmail);
        System.out.println("Any salary > 100k: " + anyHighSalary);
    }

    public static void checkEmployeesSinglePass(List<Employee> employees) {

        // Mutable accumulator — holds both results as we iterate once
        class Accumulator {
            boolean allHaveEmail  = true;
            boolean anyHighSalary = false;
        }

        Accumulator result = employees.stream()
                .collect(
                        Collector.of(
                                Accumulator::new,                                    // supplier
                                (acc, e) -> {                                        // accumulator
                                    if (e.getDepartment() == null) acc.allHaveEmail = false;
                                    if (e.getSalary() > 100000) acc.anyHighSalary = true;
                                },
                                (a, b) -> {                                          // combiner (for parallel)
                                    a.allHaveEmail  = a.allHaveEmail && b.allHaveEmail;
                                    a.anyHighSalary = a.anyHighSalary || b.anyHighSalary;
                                    return a;
                                }
                        )
                );

        System.out.println("All have email:    " + result.allHaveEmail);
        System.out.println("Any salary > 100k: " + result.anyHighSalary);
    }

    public static void checkWithReduce(List<Employee> employees) {

        // Identity: [allEmail=true, anySalary=false]
        boolean[] flags = employees.stream()
                .reduce(
                        new boolean[]{true, false},         // identity: [allEmail, anyHighSalary]
                        (acc, e) -> new boolean[]{
                                acc[0] && e.getDepartment() != null,
                                acc[1] || e.getSalary() > 100000
                        },
                        (a, b) -> new boolean[]{
                                a[0] && b[0],
                                a[1] || b[1]
                        }
                );

        System.out.println("All have email:    " + flags[0]);
        System.out.println("Any salary > 100k: " + flags[1]);
    }

}

