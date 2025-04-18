package practice;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Use case to analyze student scores using streams.
 */
public class StudentScoreAnalytics {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
            new Student("John", Arrays.asList(85, 92, 88)),
            new Student("Jane", Arrays.asList(78, 74, 80)),
            new Student("Jack", Arrays.asList(90, 91, 95)),
            new Student("Jill", Arrays.asList(70, 65, 60))
        );

        // Average score of each student
        System.out.println("Student average scores:");
        students.forEach(student -> {
            double avg = student.getScores().stream().mapToInt(Integer::intValue).average().orElse(0);
            System.out.println(student.getName() + ": " + avg);
        });

        // Students with average > 85
        System.out.println("\nTop performing students:");
        students.stream()
                .filter(s -> s.getScores().stream().mapToInt(Integer::intValue).average().orElse(0) > 85)
                .forEach(s -> System.out.println(s.getName()));
    }
}

class Student {
    private String name;
    private List<Integer> scores;

    public Student(String name, List<Integer> scores) {
        this.name = name;
        this.scores = scores;
    }

    public String getName() { return name; }
    public List<Integer> getScores() { return scores; }
}