package methodreferences;

import java.util.Arrays;
import java.util.List;

public class MethodReferenceExamples {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Java", "Lambda", "Stream");

        names.forEach(System.out::println);
        names.forEach(MethodReferenceExamples::printUpperCase);
    }

    static void printUpperCase(String str) {
        System.out.println(str.toUpperCase());
    }
}