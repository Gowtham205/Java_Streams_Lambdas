package lambdas;

@FunctionalInterface
interface Greeting {
    void sayHello(String name);
}

public class LambdaWithFunctionalInterface {
    public static void main(String[] args) {
        Greeting greet = (name) -> System.out.println("Hello, " + name);
        greet.sayHello("Java Developer");
    }
}