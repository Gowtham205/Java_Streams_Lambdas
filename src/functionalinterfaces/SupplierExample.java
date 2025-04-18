package functionalinterfaces;

import java.util.function.Supplier;
import java.util.Random;

public class SupplierExample {
    public static void main(String[] args) {
        Supplier<Integer> randomNumber = () -> new Random().nextInt(100);
        System.out.println("Generated: " + randomNumber.get());
    }
}