package streams;

import java.util.Arrays;
import java.util.List;

import java.util.*;
import java.util.stream.*;
import java.util.function.Function;

/**
 * Complete reference for Java 8+ Stream Terminal Operations
 *
 * KEY RULES:
 *   • Terminal operations TRIGGER the pipeline — nothing runs until one is called
 *   • Once a terminal op is called, the stream is CLOSED and cannot be reused
 *   • Short-circuit terminals (findFirst, anyMatch, limit) can stop early
 *   • Every stream pipeline has EXACTLY ONE terminal operation
 *
 * CATEGORIES:
 *   Iteration   → forEach, forEachOrdered
 *   Aggregation → count, sum, min, max, average, summaryStatistics
 *   Reduction   → reduce
 *   Collection  → collect  (see StreamCollectorsExample for full Collectors coverage)
 *   Search      → findFirst, findAny
 *   Matching    → anyMatch, allMatch, noneMatch
 *   Conversion  → toArray
 *
 * MODEL: Employee(name, department, salary, active)
 */
public class StreamTerminalOperations {

    // ─────────────────────────────────────────────────────────────
    // Model class
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

        @Override
        public String toString() {
            return name + "(" + department + ", $" + (int) salary + ")";
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Sample data
    // ─────────────────────────────────────────────────────────────
    static List<Employee> employees = Arrays.asList(
            new Employee("Alice",  "Engineering", 120000, true),
            new Employee("Bob",    "Marketing",    85000, true),
            new Employee("Carol",  "Engineering",  95000, false),
            new Employee("Dave",   "HR",           60000, true),
            new Employee("Eve",    "Marketing",    92000, true),
            new Employee("Frank",  "HR",           72000, false),
            new Employee("Grace",  "Engineering", 110000, true),
            new Employee("Heidi",  "Marketing",    78000, true)
    );

    static List<String> fruits = Arrays.asList(
            "apple", "banana", "orange", "apple", "mango", "banana"
    );

    static List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);

    public static void main(String[] args) {

        // ══════════════════════════════════════════════════════════
        // 1. forEach(Consumer)
        //    — iterates every element; performs a side-effect
        //    — does NOT return a value (void)
        //    — order is NOT guaranteed in parallelStream()
        // ══════════════════════════════════════════════════════════
        System.out.println("1. forEach()");

        // Basic print
        System.out.print("   Fruits: ");
        fruits.stream()
                .distinct()
                .forEach(f -> System.out.print(f + " "));
        System.out.println();

        // With method reference
        System.out.println("   Employee names:");
        employees.stream()
                .filter(Employee::isActive)
                .map(Employee::getName)
                .forEach(name -> System.out.println("     → " + name));

        // forEach vs for-loop: use forEach for read-only side effects (printing, logging)
        // Never use forEach to mutate an external list — race conditions in parallel streams


        // ══════════════════════════════════════════════════════════
        // 2. forEachOrdered(Consumer)
        //    — same as forEach but GUARANTEES encounter order
        //    — critical when using parallelStream() and order matters
        // ══════════════════════════════════════════════════════════
        System.out.println("\n2. forEachOrdered()");

        System.out.print("   Sequential forEach:        ");
        fruits.stream()
                .forEach(f -> System.out.print(f + " "));
        System.out.println();

        System.out.print("   Parallel forEachOrdered:   ");
        fruits.parallelStream()
                .forEachOrdered(f -> System.out.print(f + " "));
        System.out.println();
        // forEachOrdered preserves order even in parallel; forEach may not


        // ══════════════════════════════════════════════════════════
        // 3. count()
        //    — returns the number of elements as a long
        //    — short-circuits when stream has SIZED characteristic
        // ══════════════════════════════════════════════════════════
        System.out.println("\n3. count()");

        long uniqueFruits = fruits.stream()
                .distinct()
                .count();
        System.out.println("   Unique fruits:              " + uniqueFruits);

        long activeHighEarners = employees.stream()
                .filter(Employee::isActive)
                .filter(e -> e.getSalary() > 90000)
                .count();
        System.out.println("   Active employees > $90k:    " + activeHighEarners);

        // Count per department (as downstream — covered in Collectors class)
        Map<String, Long> countByDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment, Collectors.counting()
                ));
        System.out.println("   Count by department:        " + countByDept);


        // ══════════════════════════════════════════════════════════
        // 4. collect(Collector)
        //    — most powerful terminal op; converts stream into a container
        //    — see StreamCollectorsExample for the full Collectors API
        //    — shown here with the most common real-world patterns
        // ══════════════════════════════════════════════════════════
        System.out.println("\n4. collect()");

        // → List
        List<String> activeNames = employees.stream()
                .filter(Employee::isActive)
                .map(Employee::getName)
                .collect(Collectors.toList());
        System.out.println("   Active names (List):   " + activeNames);

        // → Set (deduplication)
        Set<String> departments = employees.stream()
                .map(Employee::getDepartment)
                .collect(Collectors.toSet());
        System.out.println("   Departments (Set):     " + departments);

        // → Map
        Map<String, Double> nameSalary = employees.stream()
                .collect(Collectors.toMap(
                        Employee::getName,
                        Employee::getSalary
                ));
        System.out.println("   Name → Salary (Map):   " + nameSalary);

        // → String (joining)
        String csv = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("   Joined names:          " + csv);

        // → grouped Map
        Map<String, List<Employee>> grouped = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        System.out.println("   Grouped by dept keys:  " + grouped.keySet());


        // ══════════════════════════════════════════════════════════
        // 5. reduce()  —  3 variants
        //    — combines all elements into a single value using a BinaryOperator
        //    — fold/aggregate operation from functional programming
        // ══════════════════════════════════════════════════════════
        System.out.println("\n5. reduce()");

        // 5a. reduce(identity, BinaryOperator) → T  (no Optional, identity is default)
        int sumWithIdentity = numbers.stream()
                .reduce(0, Integer::sum);
        System.out.println("   Sum (with identity):      " + sumWithIdentity);

        double totalSalary = employees.stream()
                .map(Employee::getSalary)
                .reduce(0.0, Double::sum);
        System.out.println("   Total salary:             $" + totalSalary);

        // 5b. reduce(BinaryOperator) → Optional<T>  (no identity — empty stream safe)
        Optional<Integer> maxNum = numbers.stream()
                .reduce(Integer::max);
        maxNum.ifPresent(m -> System.out.println("   Max number:               " + m));

        Optional<Employee> highestPaid = employees.stream()
                .reduce((a, b) -> a.getSalary() > b.getSalary() ? a : b);
        highestPaid.ifPresent(e -> System.out.println("   Highest paid:             " + e));

        // 5c. reduce(identity, mapper, combiner) → for parallel streams
        //     combiner merges partial results from different threads
        double parallelSum = employees.parallelStream()
                .reduce(
                        0.0,                            // identity
                        (sum, e) -> sum + e.getSalary(), // accumulator: fold each element
                        Double::sum                      // combiner: merge thread results
                );
        System.out.println("   Parallel salary sum:      $" + parallelSum);

        // String concatenation with reduce (show why joining() is preferred)
        String concat = employees.stream()
                .map(Employee::getName)
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
        System.out.println("   Name concat (reduce):     " + concat);


        // ══════════════════════════════════════════════════════════
        // 6. min(Comparator) / max(Comparator)
        //    — returns Optional<T>; empty if stream is empty
        //    — equivalent to reduce with min/max logic
        // ══════════════════════════════════════════════════════════
        System.out.println("\n6. min() / max()");

        Optional<Employee> lowestPaid = employees.stream()
                .min(Comparator.comparingDouble(Employee::getSalary));
        lowestPaid.ifPresent(e -> System.out.println("   Lowest paid:   " + e));

        Optional<Employee> highestPaid2 = employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
        highestPaid2.ifPresent(e -> System.out.println("   Highest paid:  " + e));

        // On a primitive stream
        OptionalInt maxNumber = numbers.stream()
                .mapToInt(Integer::intValue)
                .max();
        maxNumber.ifPresent(m -> System.out.println("   Max number:    " + m));

        OptionalInt minNumber = numbers.stream()
                .mapToInt(Integer::intValue)
                .min();
        minNumber.ifPresent(m -> System.out.println("   Min number:    " + m));

        // min/max on empty stream → Optional.empty() — never throws
        Optional<String> emptyMin = Stream.<String>empty().min(Comparator.naturalOrder());
        System.out.println("   Empty stream min:  " + emptyMin);   // Optional.empty


        // ══════════════════════════════════════════════════════════
        // 7. sum() / average()  — on primitive streams only
        //    — available on IntStream, LongStream, DoubleStream
        //    — NOT available on Stream<T>; use mapToInt/Double first
        // ══════════════════════════════════════════════════════════
        System.out.println("\n7. sum() / average()  [primitive streams]");

        double salarySum = employees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
        System.out.println("   Total salary:      $" + salarySum);

        OptionalDouble avgSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .average();
        avgSalary.ifPresent(a -> System.out.printf("   Avg salary:        $%.2f%n", a));

        int numSum = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println("   Number sum:        " + numSum);


        // ══════════════════════════════════════════════════════════
        // 8. summaryStatistics()  — on primitive streams only
        //    — returns count, sum, min, max, average in ONE terminal call
        //    — avoids 5 separate stream passes
        // ══════════════════════════════════════════════════════════
        System.out.println("\n8. summaryStatistics()  [primitive streams]");

        DoubleSummaryStatistics stats = employees.stream()
                .mapToDouble(Employee::getSalary)
                .summaryStatistics();

        System.out.println("   Count:    " + stats.getCount());
        System.out.println("   Sum:      $" + stats.getSum());
        System.out.println("   Min:      $" + stats.getMin());
        System.out.println("   Max:      $" + stats.getMax());
        System.out.printf( "   Average:  $%.2f%n", stats.getAverage());

        IntSummaryStatistics numStats = numbers.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        System.out.println("   Number stats: " + numStats);


        // ══════════════════════════════════════════════════════════
        // 9. findFirst()
        //    — returns Optional<T> of the FIRST element in encounter order
        //    — short-circuits: stops the pipeline immediately on finding one
        //    — use on sequential streams when order matters
        // ══════════════════════════════════════════════════════════
        System.out.println("\n9. findFirst()");

        Optional<Employee> firstActive = employees.stream()
                .filter(Employee::isActive)
                .findFirst();
        firstActive.ifPresent(e -> System.out.println("   First active:              " + e));

        // Common pattern: findFirst + orElse/orElseThrow
        Employee firstEngineering = employees.stream()
                .filter(e -> "Engineering".equals(e.getDepartment()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Engineering employee found"));
        System.out.println("   First Engineering:         " + firstEngineering);

        // findFirst on empty filtered stream → Optional.empty()
        Optional<Employee> noResult = employees.stream()
                .filter(e -> e.getSalary() > 999999)
                .findFirst();
        System.out.println("   Not found (orElse null):   " + noResult.orElse(null));


        // ══════════════════════════════════════════════════════════
        // 10. findAny()
        //     — returns Optional<T> of ANY element (non-deterministic)
        //     — short-circuits: returns whichever thread finishes first
        //     — PREFER over findFirst() in parallelStream() for performance
        // ══════════════════════════════════════════════════════════
        System.out.println("\n10. findAny()");

        // Sequential — behaves like findFirst in practice
        Optional<Employee> anyMarketing = employees.stream()
                .filter(e -> "Marketing".equals(e.getDepartment()))
                .findAny();
        anyMarketing.ifPresent(e -> System.out.println("   Any Marketing (seq):       " + e));

        // Parallel — returns whichever thread wins; no guaranteed order
        Optional<Employee> anyParallel = employees.parallelStream()
                .filter(e -> e.getSalary() > 90000)
                .findAny();
        anyParallel.ifPresent(e -> System.out.println("   Any > $90k (parallel):     " + e));
        // Result may differ each run — that's expected and correct


        // ══════════════════════════════════════════════════════════
        // 11. anyMatch(Predicate)
        //     — returns true if AT LEAST ONE element matches
        //     — short-circuits: stops on first match
        //     — returns false for empty streams
        // ══════════════════════════════════════════════════════════
        System.out.println("\n11. anyMatch()");

        boolean hasHighEarner = employees.stream()
                .anyMatch(e -> e.getSalary() > 100000);
        System.out.println("   Any salary > $100k:        " + hasHighEarner);

        boolean hasInactive = employees.stream()
                .anyMatch(e -> !e.isActive());
        System.out.println("   Any inactive:              " + hasInactive);

        boolean anyEmpty = Stream.empty()
                .anyMatch(x -> true);
        System.out.println("   Empty stream anyMatch:     " + anyEmpty);  // always false


        // ══════════════════════════════════════════════════════════
        // 12. allMatch(Predicate)
        //     — returns true if EVERY element matches
        //     — short-circuits: stops on first NON-match
        //     — returns true for empty streams (vacuous truth — know this cold!)
        // ══════════════════════════════════════════════════════════
        System.out.println("\n12. allMatch()");

        boolean allHaveNames = employees.stream()
                .allMatch(e -> e.getName() != null && !e.getName().isEmpty());
        System.out.println("   All have names:            " + allHaveNames);

        boolean allHighSalary = employees.stream()
                .allMatch(e -> e.getSalary() > 50000);
        System.out.println("   All salary > $50k:         " + allHighSalary);

        // TRAP: empty stream → always true (vacuous truth)
        boolean emptyAllMatch = Stream.empty()
                .allMatch(x -> false);
        System.out.println("   Empty stream allMatch:     " + emptyAllMatch);  // true!


        // ══════════════════════════════════════════════════════════
        // 13. noneMatch(Predicate)
        //     — returns true if NO element matches the predicate
        //     — short-circuits: stops on first match
        //     — returns true for empty streams
        // ══════════════════════════════════════════════════════════
        System.out.println("\n13. noneMatch()");

        boolean noNegativeSalary = employees.stream()
                .noneMatch(e -> e.getSalary() < 0);
        System.out.println("   No negative salaries:      " + noNegativeSalary);

        boolean noExternalDept = employees.stream()
                .noneMatch(e -> "Extern".equals(e.getDepartment()));
        System.out.println("   No 'Extern' department:    " + noExternalDept);

        // Relationship: noneMatch(p) == !anyMatch(p) == allMatch(p.negate())
        boolean check1 = employees.stream().noneMatch(e -> e.getSalary() > 200000);
        boolean check2 = !employees.stream().anyMatch(e -> e.getSalary() > 200000);
        System.out.println("   noneMatch == !anyMatch:    " + (check1 == check2));


        // ══════════════════════════════════════════════════════════
        // 14. toArray()  — 2 variants
        //     — converts stream to an array instead of a collection
        // ══════════════════════════════════════════════════════════
        System.out.println("\n14. toArray()");

        // 14a. No args → Object[] (requires casting)
        Object[] objArray = employees.stream()
                .map(Employee::getName)
                .toArray();
        System.out.println("   Object[]:   " + Arrays.toString(objArray));

        // 14b. With IntFunction<T[]> → typed array (preferred)
        String[] nameArray = employees.stream()
                .map(Employee::getName)
                .toArray(String[]::new);        // String[]::new is IntFunction<String[]>
        System.out.println("   String[]:   " + Arrays.toString(nameArray));

        // Primitive array via IntStream
        int[] salaryArray = employees.stream()
                .mapToInt(e -> (int) e.getSalary())
                .toArray();
        System.out.println("   int[]:      " + Arrays.toString(salaryArray));


        // ══════════════════════════════════════════════════════════
        // 15. iterator() / spliterator()
        //     — escape hatch: get an Iterator from a stream
        //     — use only when APIs require Iterator (e.g. legacy code)
        //     — once called, stream is consumed
        // ══════════════════════════════════════════════════════════
        System.out.println("\n15. iterator() / spliterator()");

        Iterator<String> iter = employees.stream()
                .map(Employee::getName)
                .iterator();

        System.out.print("   Via iterator: ");
        while (iter.hasNext()) {
            System.out.print(iter.next() + " ");
        }
        System.out.println();
        // Use sparingly — defeats the purpose of streams


        // ══════════════════════════════════════════════════════════
        // 16. Matching operations — interview comparison table
        // ══════════════════════════════════════════════════════════
        System.out.println("\n16. Matching — all three compared");

        List<Integer> mixed = Arrays.asList(2, 4, 6, 7, 8);

        boolean any  = mixed.stream().anyMatch(n -> n % 2 != 0);
        boolean all  = mixed.stream().allMatch(n -> n % 2 == 0);
        boolean none = mixed.stream().noneMatch(n -> n < 0);

        System.out.println("   List: " + mixed);
        System.out.println("   anyMatch(odd):        " + any);   // true  — 7 is odd
        System.out.println("   allMatch(even):       " + all);   // false — 7 is odd
        System.out.println("   noneMatch(negative):  " + none);  // true  — no negatives

        // Empty stream behaviour (interview trap)
        System.out.println("\n   --- Empty stream behaviour ---");
        System.out.println("   empty.anyMatch(true):  " + Stream.empty().anyMatch(x -> true));  // false
        System.out.println("   empty.allMatch(false): " + Stream.empty().allMatch(x -> false)); // true ← trap!
        System.out.println("   empty.noneMatch(true): " + Stream.empty().noneMatch(x -> true)); // true


        // ══════════════════════════════════════════════════════════
        // QUICK REFERENCE SUMMARY
        // ══════════════════════════════════════════════════════════
        System.out.println("\n══════════════════════════════════════════════════════════");
        System.out.println("  TERMINAL OPERATIONS QUICK REFERENCE");
        System.out.println("══════════════════════════════════════════════════════════");
        System.out.println("  ITERATION (void — side effects only)");
        System.out.println("    forEach(Consumer)         → unordered; unsafe in parallel");
        System.out.println("    forEachOrdered(Consumer)  → preserves order in parallel");
        System.out.println();
        System.out.println("  AGGREGATION");
        System.out.println("    count()                   → long");
        System.out.println("    sum()            [prim]   → numeric total");
        System.out.println("    average()        [prim]   → OptionalDouble");
        System.out.println("    min(Comparator)           → Optional<T>");
        System.out.println("    max(Comparator)           → Optional<T>");
        System.out.println("    summaryStatistics()[prim] → count+sum+min+max+avg at once");
        System.out.println();
        System.out.println("  REDUCTION");
        System.out.println("    reduce(identity, op)      → T           (identity = default)");
        System.out.println("    reduce(op)                → Optional<T> (safe on empty stream)");
        System.out.println("    reduce(id, mapper, comb)  → for parallel streams");
        System.out.println();
        System.out.println("  COLLECTION");
        System.out.println("    collect(Collector)        → List/Set/Map/String/any container");
        System.out.println("    toArray()                 → Object[]");
        System.out.println("    toArray(T[]::new)         → typed T[]   (preferred)");
        System.out.println();
        System.out.println("  SEARCH  (short-circuit)");
        System.out.println("    findFirst()               → Optional<T>; respects order");
        System.out.println("    findAny()                 → Optional<T>; faster in parallel");
        System.out.println();
        System.out.println("  MATCHING  (short-circuit, all return boolean)");
        System.out.println("    anyMatch(Predicate)       → true if ≥1 match; false if empty");
        System.out.println("    allMatch(Predicate)       → true if all match; TRUE if empty ← trap");
        System.out.println("    noneMatch(Predicate)      → true if 0 match;  true if empty");
        System.out.println();
        System.out.println("  ESCAPE HATCH");
        System.out.println("    iterator()                → Iterator<T>  (legacy interop)");
        System.out.println("    spliterator()             → Spliterator<T> (parallel/custom)");
        System.out.println();
        System.out.println("  KEY RULES");
        System.out.println("    • Terminal op TRIGGERS the pipeline — lazy until then");
        System.out.println("    • Stream is CLOSED after terminal op — cannot reuse");
        System.out.println("    • [prim] = only on IntStream / LongStream / DoubleStream");
        System.out.println("    • Short-circuit: findFirst, findAny, anyMatch, allMatch, noneMatch");
        System.out.println("    • allMatch on empty → TRUE (vacuous truth) — know this cold");
        System.out.println("    • findAny > findFirst in parallelStream() for performance");
        System.out.println("══════════════════════════════════════════════════════════");
    }
}
