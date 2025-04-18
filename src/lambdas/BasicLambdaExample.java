package lambdas;

public class BasicLambdaExample {
    public static void main(String[] args) {
        Runnable task = () -> System.out.println("Hello from a Lambda Runnable!");
        task.run();
    }
}