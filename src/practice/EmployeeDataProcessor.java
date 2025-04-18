package practice;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Demonstrates use of Streams and Lambdas on a list of Employee objects.
 */
public class EmployeeDataProcessor {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "HR", 45000),
            new Employee("Bob", "Engineering", 75000),
            new Employee("Charlie", "Engineering", 88000),
            new Employee("David", "HR", 39000),
            new Employee("Eve", "Finance", 61000)
        );

        // 1. Filter employees with salary > 50000
        System.out.println("Employees with salary > 50000:");
        employees.stream()
                .filter(emp -> emp.getSalary() > 50000)
                .forEach(System.out::println);

        // 2. Group employees by department
        System.out.println("\nEmployees grouped by department:");
        Map<String, List<Employee>> grouped = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        grouped.forEach((dept, emps) -> {
            System.out.println(dept + ": " + emps);
        });

        // 3. Highest paid employee
        Optional<Employee> highestPaid = employees.stream()
                .max(Comparator.comparing(Employee::getSalary));
        highestPaid.ifPresent(emp -> System.out.println("\nHighest paid: " + emp));
    }
}

class Employee {
    private String name;
    private String department;
    private double salary;

    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return name + " (" + department + ", " + salary + ")";
    }
}