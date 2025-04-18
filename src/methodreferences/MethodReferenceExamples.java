package methodreferences;

import java.util.Arrays;
import java.util.List;

/**
 * Shows method references: static, instance, and constructor references.
 */
public class MethodReferenceExamples {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Java", "Lambda", "Stream");

        // Instance method reference
        names.forEach(System.out::println);

        // Static method reference
        names.forEach(MethodReferenceExamples::printUpperCase);
    }

    static void printUpperCase(String str) {
        System.out.println(str.toUpperCase());
    }
}
