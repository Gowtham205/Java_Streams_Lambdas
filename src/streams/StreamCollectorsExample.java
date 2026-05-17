package streams;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Complete reference for Java 8+ Collectors API
 * Covers every collector method with realistic examples
 *
 * MODEL CLASSES USED:
 *   Employee(name, department, salary, age)
 */
public class StreamCollectorsExample {

    // ─────────────────────────────────────────────────────────────
    // Model class
    // ─────────────────────────────────────────────────────────────
    static class Employee {
        String name, department;
        double salary;
        int age;

        Employee(String name, String department, double salary, int age) {
            this.name       = name;
            this.department = department;
            this.salary     = salary;
            this.age        = age;
        }

        public String getName()       { return name; }
        public String getDepartment() { return department; }
        public double getSalary()     { return salary; }
        public int    getAge()        { return age; }

        @Override public String toString() {
            return name + "(" + department + ", $" + salary + ")";
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Sample data
    // ─────────────────────────────────────────────────────────────
    static List<Employee> employees = Arrays.asList(
            new Employee("Alice",   "Engineering", 120000, 32),
            new Employee("Bob",     "Marketing",    85000, 28),
            new Employee("Carol",   "Engineering",  95000, 35),
            new Employee("Dave",    "HR",           60000, 40),
            new Employee("Eve",     "Marketing",    92000, 30),
            new Employee("Frank",   "HR",           72000, 45),
            new Employee("Grace",   "Engineering", 110000, 29),
            new Employee("Heidi",   "Marketing",    78000, 33)
    );

    static List<String> words = Arrays.asList(
            "java", "stream", "collect", "lambda", "stream", "java", "filter"
    );

    public static void main(String[] args) {

        // ══════════════════════════════════════════════════════════
        // 1. toList()  —  collect into a List (preserves order)
        // ══════════════════════════════════════════════════════════
        List<String> engineeringNames = employees.stream()
                .filter(e -> e.getDepartment().equals("Engineering"))
                .map(Employee::getName)
                .collect(Collectors.toList());

        System.out.println("1. toList()");
        System.out.println("   Engineering names: " + engineeringNames);
        // [Alice, Carol, Grace]


        // ══════════════════════════════════════════════════════════
        // 2. toUnmodifiableList()  —  immutable List (Java 10+)
        // ══════════════════════════════════════════════════════════
        List<String> immutable = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toUnmodifiableList());

        System.out.println("\n2. toUnmodifiableList()");
        System.out.println("   Names (immutable): " + immutable);
        // Adding to this list throws UnsupportedOperationException


        // ══════════════════════════════════════════════════════════
        // 3. toSet()  —  collect into a Set (removes duplicates)
        // ══════════════════════════════════════════════════════════
        Set<String> uniqueWords = words.stream()
                .collect(Collectors.toSet());

        System.out.println("\n3. toSet()");
        System.out.println("   Unique words: " + uniqueWords);
        // {java, stream, collect, lambda, filter}  — no duplicates


        // ══════════════════════════════════════════════════════════
        // 4. toUnmodifiableSet()  —  immutable Set (Java 10+)
        // ══════════════════════════════════════════════════════════
        Set<String> immutableSet = words.stream()
                .collect(Collectors.toUnmodifiableSet());

        System.out.println("\n4. toUnmodifiableSet()");
        System.out.println("   Unique words (immutable): " + immutableSet);


        // ══════════════════════════════════════════════════════════
        // 5. toCollection()  —  collect into a specific Collection type
        // ══════════════════════════════════════════════════════════
        LinkedList<String> linkedList = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(LinkedList::new));

        TreeSet<String> sortedSet = employees.stream()
                .map(Employee::getDepartment)
                .collect(Collectors.toCollection(TreeSet::new));

        System.out.println("\n5. toCollection()");
        System.out.println("   LinkedList: " + linkedList);
        System.out.println("   TreeSet (sorted, no dups): " + sortedSet);
        // TreeSet: [Engineering, HR, Marketing]


        // ══════════════════════════════════════════════════════════
        // 6. joining()  —  3 variants
        // ══════════════════════════════════════════════════════════

        // 6a. No args — simple concat
        String concat = words.stream()
                .collect(Collectors.joining());

        // 6b. Delimiter only
        String csv = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", "));

        // 6c. Delimiter + prefix + suffix
        String formatted = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", ", "[", "]"));

        System.out.println("\n6. joining()");
        System.out.println("   No args:              " + concat);
        System.out.println("   Delimiter:            " + csv);
        System.out.println("   Prefix+Suffix:        " + formatted);


        // ══════════════════════════════════════════════════════════
        // 7. counting()  —  count elements (as downstream collector)
        // ══════════════════════════════════════════════════════════
        long total = employees.stream()
                .collect(Collectors.counting());

        // Counting per department (used as downstream)
        Map<String, Long> countByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.counting()
                ));

        System.out.println("\n7. counting()");
        System.out.println("   Total employees:  " + total);
        System.out.println("   Count by dept:    " + countByDept);
        // {Engineering=3, HR=2, Marketing=3}


        // ══════════════════════════════════════════════════════════
        // 8. summingInt / summingLong / summingDouble
        // ══════════════════════════════════════════════════════════
        double totalSalary = employees.stream()
                .collect(Collectors.summingDouble(Employee::getSalary));

        // Per department
        Map<String, Double> salaryByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summingDouble(Employee::getSalary)
                ));

        System.out.println("\n8. summingDouble()");
        System.out.println("   Total salary:      $" + totalSalary);
        System.out.println("   Salary by dept:    " + salaryByDept);


        // ══════════════════════════════════════════════════════════
        // 9. averagingInt / averagingLong / averagingDouble
        // ══════════════════════════════════════════════════════════
        double avgSalary = employees.stream()
                .collect(Collectors.averagingDouble(Employee::getSalary));

        Map<String, Double> avgAgeByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingInt(Employee::getAge)
                ));

        System.out.println("\n9. averagingDouble()");
        System.out.println("   Avg salary:        $" + avgSalary);
        System.out.println("   Avg age by dept:   " + avgAgeByDept);


        // ══════════════════════════════════════════════════════════
        // 10. summarizingInt / summarizingDouble
        //     → gives count, sum, min, max, average in one shot
        // ══════════════════════════════════════════════════════════
        DoubleSummaryStatistics salaryStats = employees.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));

        System.out.println("\n10. summarizingDouble()");
        System.out.println("    Count:   " + salaryStats.getCount());
        System.out.println("    Sum:     $" + salaryStats.getSum());
        System.out.println("    Min:     $" + salaryStats.getMin());
        System.out.println("    Max:     $" + salaryStats.getMax());
        System.out.println("    Average: $" + salaryStats.getAverage());


        // ══════════════════════════════════════════════════════════
        // 11. minBy() / maxBy()  —  find min or max element
        // ══════════════════════════════════════════════════════════
        Optional<Employee> lowestPaid = employees.stream()
                .collect(Collectors.minBy(
                        Comparator.comparingDouble(Employee::getSalary)
                ));

        Optional<Employee> highestPaid = employees.stream()
                .collect(Collectors.maxBy(
                        Comparator.comparingDouble(Employee::getSalary)
                ));

        System.out.println("\n11. minBy() / maxBy()");
        lowestPaid .ifPresent(e -> System.out.println("    Lowest paid:   " + e));
        highestPaid.ifPresent(e -> System.out.println("    Highest paid:  " + e));


        // ══════════════════════════════════════════════════════════
        // 12. groupingBy()  —  3 variants
        // ══════════════════════════════════════════════════════════

        // 12a. Single classifier — Map<K, List<V>>
        Map<String, List<Employee>> byDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));

        // 12b. With downstream collector
        Map<String, List<String>> namesByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())
                ));

        // 12c. With specific Map type (TreeMap = sorted keys)
        Map<String, Long> sortedCountByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        TreeMap::new,
                        Collectors.counting()
                ));

        System.out.println("\n12. groupingBy()");
        System.out.println("    By dept (employees): " + byDept.keySet());
        System.out.println("    Names by dept:        " + namesByDept);
        System.out.println("    Sorted count by dept: " + sortedCountByDept);


        // ══════════════════════════════════════════════════════════
        // 13. groupingByConcurrent()  —  thread-safe groupingBy
        //     Use with parallelStream() only
        // ══════════════════════════════════════════════════════════
        Map<String, List<Employee>> concurrentGroup = employees.parallelStream()
                .collect(Collectors.groupingByConcurrent(Employee::getDepartment));

        System.out.println("\n13. groupingByConcurrent()");
        System.out.println("    Departments: " + concurrentGroup.keySet());


        // ══════════════════════════════════════════════════════════
        // 14. partitioningBy()  —  splits into true/false groups
        // ══════════════════════════════════════════════════════════

        // 14a. Simple partition
        Map<Boolean, List<Employee>> highLowSalary = employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.getSalary() > 90000
                ));

        // 14b. With downstream collector
        Map<Boolean, Long> partitionCount = employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.getSalary() > 90000,
                        Collectors.counting()
                ));

        System.out.println("\n14. partitioningBy()");
        System.out.println("    High salary (>90k): " + highLowSalary.get(true));
        System.out.println("    Low salary  (≤90k): " + highLowSalary.get(false));
        System.out.println("    Counts: " + partitionCount);
        // {false=3, true=5}


        // ══════════════════════════════════════════════════════════
        // 15. toMap()  —  3 variants
        // ══════════════════════════════════════════════════════════

        // 15a. key + value mapper (fails on duplicate keys!)
        Map<String, Double> nameSalaryMap = employees.stream()
                .collect(Collectors.toMap(
                        Employee::getName,
                        Employee::getSalary
                ));

        // 15b. With merge function (handles duplicate keys)
        Map<String, Double> deptTotalSalary = employees.stream()
                .collect(Collectors.toMap(
                        Employee::getDepartment,
                        Employee::getSalary,
                        Double::sum            // merge: sum salaries on duplicate dept keys
                ));

        // 15c. With merge function + specific Map type
        Map<String, Double> sortedDeptSalary = employees.stream()
                .collect(Collectors.toMap(
                        Employee::getDepartment,
                        Employee::getSalary,
                        Double::sum,
                        TreeMap::new           // sorted by key
                ));

        System.out.println("\n15. toMap()");
        System.out.println("    Name → Salary:        " + nameSalaryMap);
        System.out.println("    Dept → Total Salary:  " + deptTotalSalary);
        System.out.println("    Sorted dept salary:   " + sortedDeptSalary);


        // ══════════════════════════════════════════════════════════
        // 16. toUnmodifiableMap()  —  immutable Map (Java 10+)
        // ══════════════════════════════════════════════════════════
        Map<String, Double> immutableMap = employees.stream()
                .collect(Collectors.toUnmodifiableMap(
                        Employee::getName,
                        Employee::getSalary
                ));

        System.out.println("\n16. toUnmodifiableMap()");
        System.out.println("    Alice's salary: $" + immutableMap.get("Alice"));


        // ══════════════════════════════════════════════════════════
        // 17. mapping()  —  transform elements before collecting
        //     Always used as a downstream collector inside groupingBy etc.
        // ══════════════════════════════════════════════════════════
        Map<String, Set<String>> nameSetByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toSet())
                ));

        System.out.println("\n17. mapping()");
        System.out.println("    Name sets by dept: " + nameSetByDept);


        // ══════════════════════════════════════════════════════════
        // 18. filtering()  —  filter inside downstream (Java 9+)
        //     Different from stream.filter(): keeps empty groups in map
        // ══════════════════════════════════════════════════════════
        Map<String, List<Employee>> highEarnersByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.filtering(
                                e -> e.getSalary() > 90000,
                                Collectors.toList()
                        )
                ));

        System.out.println("\n18. filtering() [Java 9+]");
        System.out.println("    High earners by dept: " + highEarnersByDept);
        // HR shows as empty list [] — stream.filter() would omit HR entirely


        // ══════════════════════════════════════════════════════════
        // 19. flatMapping()  —  flatten + collect downstream (Java 9+)
        // ══════════════════════════════════════════════════════════
        Map<String, List<Character>> initials = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.flatMapping(
                                e -> e.getName().chars()
                                        .limit(1)
                                        .mapToObj(c -> (char) c),
                                Collectors.toList()
                        )
                ));

        System.out.println("\n19. flatMapping() [Java 9+]");
        System.out.println("    First initials by dept: " + initials);


        // ══════════════════════════════════════════════════════════
        // 20. collectingAndThen()  —  post-process after collecting
        // ══════════════════════════════════════════════════════════

        // 20a. Wrap result in unmodifiable list
        List<String> unmodifiableNames = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList   // finisher
                ));

        // 20b. Get max salary as a double (unwrap Optional)
        double maxSalary = employees.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)),
                        opt -> opt.map(Employee::getSalary).orElse(0.0)
                ));

        System.out.println("\n20. collectingAndThen()");
        System.out.println("    Unmodifiable names: " + unmodifiableNames);
        System.out.println("    Max salary (unwrapped): $" + maxSalary);


        // ══════════════════════════════════════════════════════════
        // 21. reducing()  —  generalised reduction downstream
        //     Use stream.reduce() at top level; this is for downstream only
        // ══════════════════════════════════════════════════════════

        // 21a. Identity + BinaryOperator
        Optional<Employee> richest = employees.stream()
                .collect(Collectors.reducing(
                        (a, b) -> a.getSalary() > b.getSalary() ? a : b
                ));

        // 21b. Identity + BinaryOperator (with seed)
        double salarySum = employees.stream()
                .collect(Collectors.reducing(
                        0.0,
                        Employee::getSalary,
                        Double::sum
                ));

        // 21c. As downstream — max salary per department
        Map<String, Optional<Employee>> richestByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.reducing(
                                (a, b) -> a.getSalary() > b.getSalary() ? a : b
                        )
                ));

        System.out.println("\n21. reducing()");
        richest.ifPresent(e -> System.out.println("    Overall richest: " + e));
        System.out.println("    Salary sum: $" + salarySum);
        System.out.println("    Richest by dept: " + richestByDept);


        // ══════════════════════════════════════════════════════════
        // 22. teeing()  —  fan-out to TWO collectors, merge results (Java 12+)
        //     Processes stream ONCE, feeds into two downstream collectors
        // ══════════════════════════════════════════════════════════
        double[] minMax = employees.stream()
                .collect(Collectors.teeing(
                        Collectors.minBy(Comparator.comparingDouble(Employee::getSalary)),
                        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)),
                        (min, max) -> new double[]{
                                min.map(Employee::getSalary).orElse(0.0),
                                max.map(Employee::getSalary).orElse(0.0)
                        }
                ));

        System.out.println("\n22. teeing() [Java 12+]");
        System.out.println("    Min salary: $" + minMax[0]);
        System.out.println("    Max salary: $" + minMax[1]);


        // ══════════════════════════════════════════════════════════
        // 23. Collector.of()  —  build a fully custom Collector
        //     4 parts: supplier, accumulator, combiner, finisher
        // ══════════════════════════════════════════════════════════
        String customResult = employees.stream()
                .collect(Collector.of(
                        StringBuilder::new,                             // supplier
                        (sb, e) -> {                                    // accumulator
                            if (sb.length() > 0) sb.append(" | ");
                            sb.append(e.getName()).append(":$").append((int)e.getSalary());
                        },
                        (sb1, sb2) -> sb1.append(sb2),                // combiner (parallel)
                        StringBuilder::toString                        // finisher
                ));

        System.out.println("\n23. Collector.of() — custom collector");
        System.out.println("    " + customResult);


        // ══════════════════════════════════════════════════════════
        // QUICK REFERENCE SUMMARY
        // ══════════════════════════════════════════════════════════
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("  COLLECTORS QUICK REFERENCE");
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  ACCUMULATION");
        System.out.println("    toList()                 → List (ordered, mutable)");
        System.out.println("    toUnmodifiableList()     → List (ordered, immutable)");
        System.out.println("    toSet()                  → Set  (no duplicates)");
        System.out.println("    toUnmodifiableSet()      → Set  (immutable)");
        System.out.println("    toCollection(Supplier)   → any Collection type");
        System.out.println("    toMap(k, v)              → Map  (throws on dup keys)");
        System.out.println("    toMap(k, v, merge)       → Map  (safe dup keys)");
        System.out.println("    toUnmodifiableMap(k, v)  → Map  (immutable)");
        System.out.println();
        System.out.println("  AGGREGATION");
        System.out.println("    counting()               → Long");
        System.out.println("    summingInt/Long/Double   → number");
        System.out.println("    averagingInt/Long/Double → Double");
        System.out.println("    summarizingInt/Double    → SummaryStatistics");
        System.out.println("    minBy(comparator)        → Optional<T>");
        System.out.println("    maxBy(comparator)        → Optional<T>");
        System.out.println("    joining(delim,pre,suf)   → String");
        System.out.println("    reducing(identity, fn)   → Optional<T>");
        System.out.println();
        System.out.println("  GROUPING / PARTITIONING");
        System.out.println("    groupingBy(classifier)              → Map<K, List<V>>");
        System.out.println("    groupingBy(classifier, downstream)  → Map<K, R>");
        System.out.println("    groupingByConcurrent(...)           → ConcurrentMap");
        System.out.println("    partitioningBy(predicate)           → Map<Boolean, List>");
        System.out.println("    partitioningBy(pred, downstream)    → Map<Boolean, R>");
        System.out.println();
        System.out.println("  TRANSFORMATION (downstream only)");
        System.out.println("    mapping(fn, downstream)             → transform then collect");
        System.out.println("    filtering(pred, downstream)         → filter keeping all groups");
        System.out.println("    flatMapping(fn, downstream)         → flatten then collect");
        System.out.println("    collectingAndThen(col, finisher)    → post-process result");
        System.out.println("    teeing(col1, col2, merger)          → two collectors, one pass");
        System.out.println();
        System.out.println("  CUSTOM");
        System.out.println("    Collector.of(supplier, accum, combiner, finisher)");
        System.out.println("═══════════════════════════════════════════════════");
    }
}
