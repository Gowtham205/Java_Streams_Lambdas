package functionalinterfaces;

import java.util.function.Consumer;

public class ConsumerExample {
    public static void main(String[] args) {
        Consumer<String> printer = name -> System.out.println("Welcome, " + name);
        printer.accept("Java Developer");
    }
}