package streams;

import java.util.Arrays;
import java.util.List;

import java.util.*;
import java.util.stream.*;
import java.util.function.Function;

/**
 * Complete reference for Java 8+ Stream Intermediate Operations
 *
 * KEY RULE: Intermediate operations are LAZY — they do nothing
 * until a terminal operation (collect, forEach, count, etc.) is called.
 *
 * MODEL CLASSES USED:
 *   Employee(name, department, salary, active)
 *   Department(name, employees)
 */
public class StreamIntermediateOperations {

    // ─────────────────────────────────────────────────────────────
    // Model classes
    // ─────────────────────────────────────────────────────────────
    static class Employee {
        String name, department;
        double salary;
        boolean active;

        Employee(String name, String department, double salary, boolean active) {
            this.name       = name;
            this.department = department;
            this.salary     = salary;
            this.active     = active;
        }

        public String  getName()       { return name; }
        public String  getDepartment() { return department; }
        public double  getSalary()     { return salary; }
        public boolean isActive()      { return active; }

        @Override public String toString() {
            return name + "(" + department + ", $" + (int) salary + ")";
        }
    }

    static class Department {
        String         name;
        List<Employee> employees;

        Department(String name, List<Employee> employees) {
            this.name      = name;
            this.employees = employees;
        }

        public List<Employee> getEmployees() { return employees; }
        public String         getName()      { return name; }
    }

    // ─────────────────────────────────────────────────────────────
    // Sample data
    // ─────────────────────────────────────────────────────────────
    static List<Employee> employees = Arrays.asList(
            new Employee("Alice",   "Engineering", 120000, true),
            new Employee("Bob",     "Marketing",    85000, true),
            new Employee("Carol",   "Engineering",  95000, false),
            new Employee("Dave",    "HR",           60000, true),
            new Employee("Eve",     "Marketing",    92000, true),
            new Employee("Frank",   "HR",           72000, false),
            new Employee("Grace",   "Engineering", 110000, true),
            new Employee("Heidi",   "Marketing",    78000, true)
    );

    static List<Department> departments = Arrays.asList(
            new Department("Engineering", Arrays.asList(
                    new Employee("Alice", "Engineering", 120000, true),
                    new Employee("Grace", "Engineering", 110000, true)
            )),
            new Department("Marketing", Arrays.asList(
                    new Employee("Bob",  "Marketing", 85000, true),
                    new Employee("Eve",  "Marketing", 92000, true)
            )),
            new Department("HR", Arrays.asList(
                    new Employee("Dave",  "HR", 60000, true),
                    new Employee("Frank", "HR", 72000, false)
            ))
    );

    static List<String> names = Arrays.asList(
            "John", "Mike", "Alice", "Bob", "Alice", "John", "Charlie"
    );

    /*
      Collections.unmodifiableList()        - Decorator — wraps a mutable list, throws on writes. Back door exists.
      stream.toList() / List.of()           - Immutable class — no write methods exist at all. No back door.
      Collectors.toList()                   - No blocking — returns a plain ArrayList. Fully mutable.
     */

    public static void main(String[] args) {

        // ══════════════════════════════════════════════════════════
        // 1. filter(Predicate)
        //    — keeps elements that match the condition
        //    — short-circuits with findFirst/anyMatch
        // ══════════════════════════════════════════════════════════
        System.out.println("1. filter()");

        // Simple predicate
        List<Employee> activeEmployees = employees.stream()
                .filter(Employee::isActive)
                .collect(Collectors.toList());
        System.out.println("   Active employees: " + activeEmployees);

        // Chained predicates (AND)
        List<Employee> activeHighSalary = employees.stream()
                .filter(Employee::isActive)
                .filter(e -> e.getSalary() > 90000)
                .collect(Collectors.toList());
        System.out.println("   Active + salary > 90k: " + activeHighSalary);

        // Predicate.and() — same as chaining
        List<Employee> combined = employees.stream()
                .filter(((java.util.function.Predicate<Employee>) Employee::isActive)
                        .and(e -> e.getSalary() > 90000))
                .collect(Collectors.toList());
        System.out.println("   Predicate.and(): " + combined);


        // ══════════════════════════════════════════════════════════
        // 2. map(Function)
        //    — transforms each element, returns same count
        //    — does NOT flatten nested structures (use flatMap for that)
        // ══════════════════════════════════════════════════════════
        System.out.println("\n2. map()");

        // Object → String
        List<String> allNames = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
        System.out.println("   Names:           " + allNames);

        // String → String transform
        List<String> upperNames = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("   Uppercase:       " + upperNames);

        // Object → primitive (returns Stream<Double>, not DoubleStream)
        List<Double> salaries = employees.stream()
                .map(Employee::getSalary)
                .collect(Collectors.toList());
        System.out.println("   Salaries:        " + salaries);

        // Chained maps (pipeline of transforms)
        List<String> deptLabels = employees.stream()
                .map(Employee::getDepartment)
                .map(String::toLowerCase)
                .map(d -> "[" + d + "]")
                .collect(Collectors.toList());
        System.out.println("   Dept labels:     " + deptLabels);


        // ══════════════════════════════════════════════════════════
        // 3. mapToInt / mapToLong / mapToDouble
        //    — converts to a primitive stream (IntStream, LongStream, DoubleStream)
        //    — avoids Integer boxing; unlocks sum(), average(), range() etc.
        // ══════════════════════════════════════════════════════════
        System.out.println("\n3. mapToInt / mapToDouble");

        // mapToInt — gives IntStream with arithmetic methods
        int totalAge = employees.stream()
                .mapToInt(e -> (int) e.getSalary() / 1000)   // salary in thousands
                .sum();
        System.out.println("   Total salary (k): $" + totalAge + "k");

        OptionalDouble avgSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .average();
        avgSalary.ifPresent(a -> System.out.printf("   Avg salary:       $%.0f%n", a));

        // mapToInt then boxed() — go back to Stream<Integer>
        List<Integer> salaryThousands = employees.stream()
                .mapToInt(e -> (int) e.getSalary() / 1000)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("   Salary (k) list:  " + salaryThousands);


        // ══════════════════════════════════════════════════════════
        // 4. flatMap(Function<T, Stream<R>>)
        //    — maps each element to a stream, then FLATTENS into one stream
        //    — use when each element holds a collection
        // ══════════════════════════════════════════════════════════
        System.out.println("\n4. flatMap()");

        // Each Department → List<Employee>; flatten to single stream of employees
        List<Employee> allEmployees = departments.stream()
                .flatMap(dept -> dept.getEmployees().stream())
                .collect(Collectors.toList());
        System.out.println("   All employees (flattened): " + allEmployees);

        // Flatten List<List<String>>
        List<List<String>> nested = Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("d", "e"),
                Arrays.asList("f", "g", "h", "i")
        );
        List<String> flat = nested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println("   Flattened list:            " + flat);

        // Split sentences into individual words
        List<String> sentences = Arrays.asList(
                "Java streams are powerful",
                "flatMap is tricky"
        );
        List<String> wordList = sentences.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .collect(Collectors.toList());
        System.out.println("   Words from sentences:      " + wordList);


        // ══════════════════════════════════════════════════════════
        // 5. flatMapToInt / flatMapToDouble
        //    — flatMap + primitive stream in one step
        // ══════════════════════════════════════════════════════════
        System.out.println("\n5. flatMapToInt()");

        int totalSalaryAllDepts = departments.stream()
                .flatMapToInt(dept -> dept.getEmployees()
                        .stream()
                        .mapToInt(e -> (int) e.getSalary()))
                .sum();
        System.out.println("   Total salary across all depts: $" + totalSalaryAllDepts);


        // ══════════════════════════════════════════════════════════
        // 6. distinct()
        //    — removes duplicates using equals() + hashCode()
        //    — preserves encounter order for ordered streams
        // ══════════════════════════════════════════════════════════
        System.out.println("\n6. distinct()");

        List<String> uniqueNames = names.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("   Unique names:   " + uniqueNames);
        // ["John","Mike","Alice","Bob","Charlie"] — "John","Alice" deduplicated

        List<String> uniqueDepts = employees.stream()
                .map(Employee::getDepartment)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("   Unique depts:   " + uniqueDepts);


        // ══════════════════════════════════════════════════════════
        // 7. sorted()  — 2 variants
        //    — natural order (Comparable) or custom Comparator
        //    — stateful — must see all elements before emitting any
        // ══════════════════════════════════════════════════════════
        System.out.println("\n7. sorted()");

        // 7a. Natural order
        List<String> sortedNames = names.stream()
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        System.out.println("   Natural order:           " + sortedNames);

        // 7b. Custom Comparator — salary descending
        List<Employee> bySalaryDesc = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .collect(Collectors.toList());
        System.out.println("   By salary desc:          " + bySalaryDesc);

        // 7c. Chained Comparator — dept asc, then salary desc
        List<Employee> multiSort = employees.stream()
                .sorted(Comparator.comparing(Employee::getDepartment)
                        .thenComparing(
                                Comparator.comparingDouble(Employee::getSalary)
                                        .reversed()))
                .collect(Collectors.toList());
        System.out.println("   Dept asc + salary desc:  " + multiSort);

        // 7d. Reverse natural order on strings
        List<String> reversedNames = names.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        System.out.println("   Reverse natural order:   " + reversedNames);


        // ══════════════════════════════════════════════════════════
        // 8. limit(long n)
        //    — short-circuit: stops pipeline after n elements
        //    — very efficient on infinite streams
        // ══════════════════════════════════════════════════════════
        System.out.println("\n8. limit()");

        // Top 3 highest paid
        List<Employee> top3 = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("   Top 3 paid: " + top3);

        // First 5 numbers from infinite stream
        List<Integer> firstFive = Stream.iterate(1, n -> n + 1)
                .limit(5)
                .collect(Collectors.toList());
        System.out.println("   First 5 integers: " + firstFive);


        // ══════════════════════════════════════════════════════════
        // 9. skip(long n)
        //    — discards first n elements, passes the rest downstream
        //    — complementary to limit(); together they enable pagination
        // ══════════════════════════════════════════════════════════
        System.out.println("\n9. skip()");

        // Skip first 5, take next 3 — pagination (page 2, size 3)
        List<Employee> page2 = employees.stream()
                .skip(3)       // skip page 1
                .limit(3)      // take page 2
                .collect(Collectors.toList());
        System.out.println("   Page 2 (skip 3, limit 3): " + page2);

        // Utility method pattern for generic pagination
        // getPage(employees, pageNumber=1, pageSize=3)
        int pageSize = 3, pageNumber = 1;
        List<Employee> pageResult = employees.stream()
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
        System.out.println("   Generic page result:      " + pageResult);


        // ══════════════════════════════════════════════════════════
        // 10. peek(Consumer)
        //     — runs a side-effect on each element WITHOUT consuming it
        //     — element continues downstream unchanged
        //     — primary use: debugging pipelines
        // ══════════════════════════════════════════════════════════
        System.out.println("\n10. peek()");

        List<Employee> result = employees.stream()
                .filter(Employee::isActive)
                .peek(e -> System.out.println("    [after active filter] " + e.getName()))
                .map(e -> new Employee(
                        e.getName().toUpperCase(),
                        e.getDepartment(),
                        e.getSalary() * 1.1,   // 10% raise
                        e.isActive()))
                .peek(e -> System.out.printf(
                        "    [after map]    %s → $%.0f%n", e.getName(), e.getSalary()))
                .filter(e -> e.getSalary() > 100000)
                .peek(e -> System.out.println("    [after salary filter]    " + e.getName()))
                .collect(Collectors.toList());

        System.out.println("   Final list: " + result);
        // peek lets you see elements at each stage without breaking the pipeline


        // ══════════════════════════════════════════════════════════
        // 11. mapToObj()
        //     — converts a primitive stream back to Stream<T>
        //     — used on IntStream / LongStream / DoubleStream
        // ══════════════════════════════════════════════════════════
        System.out.println("\n11. mapToObj()");

        // IntStream → Stream<String>
        List<String> charList = IntStream.rangeClosed('A', 'E')
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.toList());
        System.out.println("   Chars A–E: " + charList);

        // Salary as formatted strings
        List<String> salaryLabels = employees.stream()
                .mapToDouble(Employee::getSalary)
                .mapToObj(s -> String.format("$%.0f", s))
                .collect(Collectors.toList());
        System.out.println("   Salary labels: " + salaryLabels);


        // ══════════════════════════════════════════════════════════
        // 12. takeWhile(Predicate)  [Java 9+]
        //     — takes elements as long as predicate is true, stops on first false
        //     — only useful on ORDERED / SORTED streams
        // ══════════════════════════════════════════════════════════
        System.out.println("\n12. takeWhile() [Java 9+]");

        List<Integer> numbers = Arrays.asList(2, 4, 6, 7, 8, 10, 12);
        List<Integer> evensUntilOdd = numbers.stream()
                .takeWhile(n -> n % 2 == 0)    // stops at 7 (first odd)
                .collect(Collectors.toList());
        System.out.println("   Evens until first odd: " + evensUntilOdd);
        // [2, 4, 6] — stops at 7, does NOT continue to 8,10,12

        List<Employee> cheapEmployees = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .takeWhile(e -> e.getSalary() < 90000)
                .collect(Collectors.toList());
        System.out.println("   Salary < 90k (sorted): " + cheapEmployees);


        // ══════════════════════════════════════════════════════════
        // 13. dropWhile(Predicate)  [Java 9+]
        //     — drops elements as long as predicate is true, keeps the rest
        //     — complement of takeWhile
        // ══════════════════════════════════════════════════════════
        System.out.println("\n13. dropWhile() [Java 9+]");

        List<Integer> afterFirstOdd = numbers.stream()
                .dropWhile(n -> n % 2 == 0)    // drops 2,4,6; keeps from 7 onward
                .collect(Collectors.toList());
        System.out.println("   After first odd: " + afterFirstOdd);
        // [7, 8, 10, 12]

        List<Employee> seniorEmployees = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary))
                .dropWhile(e -> e.getSalary() < 90000)
                .collect(Collectors.toList());
        System.out.println("   Salary >= 90k:   " + seniorEmployees);


        // ══════════════════════════════════════════════════════════
        // 14. Combining operations — realistic interview scenario
        //     "Get names of active Engineering employees earning
        //      above 100k, sorted alphabetically, top 5"
        // ══════════════════════════════════════════════════════════
        System.out.println("\n14. Combined pipeline (interview scenario)");

        List<String> topEngineers = employees.stream()
                .filter(Employee::isActive)                               // only active
                .filter(e -> "Engineering".equals(e.getDepartment()))     // only Engineering
                .filter(e -> e.getSalary() > 100000)                      // salary > 100k
                .sorted(Comparator.comparing(Employee::getName))          // alphabetical
                .limit(5)                                                  // top 5
                .map(Employee::getName)                                    // extract name
                .collect(Collectors.toList());

        System.out.println("   Top engineers: " + topEngineers);


        // ══════════════════════════════════════════════════════════
        // QUICK REFERENCE SUMMARY
        // ══════════════════════════════════════════════════════════
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("  INTERMEDIATE OPERATIONS QUICK REFERENCE");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  FILTERING");
        System.out.println("    filter(Predicate)          → keeps matching elements");
        System.out.println("    distinct()                 → removes duplicates (equals/hashCode)");
        System.out.println("    takeWhile(Predicate) [9+]  → takes while true, stops on first false");
        System.out.println("    dropWhile(Predicate) [9+]  → drops while true, keeps rest");
        System.out.println();
        System.out.println("  TRANSFORMATION");
        System.out.println("    map(Function)              → 1:1 transform, same element count");
        System.out.println("    mapToInt/Long/Double       → to primitive stream (avoids boxing)");
        System.out.println("    mapToObj()                 → primitive stream → Stream<T>");
        System.out.println("    flatMap(Function)          → 1:many + flatten (nested collections)");
        System.out.println("    flatMapToInt/Double        → flatMap + primitive stream");
        System.out.println();
        System.out.println("  ORDERING & SLICING");
        System.out.println("    sorted()                   → natural order (Comparable)");
        System.out.println("    sorted(Comparator)         → custom order; stateful");
        System.out.println("    limit(n)                   → first n elements; short-circuits");
        System.out.println("    skip(n)                    → discard first n; use with limit for paging");
        System.out.println();
        System.out.println("  DEBUGGING");
        System.out.println("    peek(Consumer)             → side-effect only; element unchanged");
        System.out.println();
        System.out.println("  KEY RULES");
        System.out.println("    • All intermediate ops are LAZY — nothing runs until terminal op");
        System.out.println("    • Stateless: filter, map, flatMap, distinct, peek");
        System.out.println("    • Stateful:  sorted, distinct (needs all elements before emitting)");
        System.out.println("    • Short-circuit: limit, takeWhile (can stop early)");
        System.out.println("    • Streams are single-use — cannot be restarted after terminal op");
        System.out.println("═══════════════════════════════════════════════════════");
    }
}
