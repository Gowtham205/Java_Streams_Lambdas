package optional;

import java.util.Optional;

/**
 * Demonstrates use of Optional to avoid NullPointerExceptions.
 */
public class OptionalExamples {
    public static void main(String[] args) {
        Optional<String> name = Optional.ofNullable(getName());

        name.ifPresentOrElse(
                val -> System.out.println("Name is: " + val),
                () -> System.out.println("Name is not available")
        );
    }

    static String getName() {
        return null; // simulate null response
    }
}
