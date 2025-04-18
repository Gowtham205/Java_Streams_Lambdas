package lambdas;

/**
 * Basic example of lambda expression usage.
 */
public class BasicLambdaExample {
    public static void main(String[] args) {
        // Using lambda to implement a Runnable
        Runnable task = () -> System.out.println("Hello from a Lambda Runnable!");
        task.run();
    }
}