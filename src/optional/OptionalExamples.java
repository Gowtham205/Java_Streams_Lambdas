package optional;

import java.util.Optional;

public class OptionalExamples {
    public static void main(String[] args) {
        Optional<String> name = Optional.ofNullable(getName());

        name.ifPresentOrElse(
            val -> System.out.println("Name is: " + val),
            () -> System.out.println("Name is not available")
        );
    }

    static String getName() {
        return null;
    }
}