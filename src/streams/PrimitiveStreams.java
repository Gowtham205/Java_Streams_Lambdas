package streams;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Example of primitive stream (IntStream).
 */
public class PrimitiveStreams {
    public static void main(String[] args) {
        IntStream.range(1, 6).forEach(System.out::println); // 1 to 5
        System.out.println();
        LongStream.range(1, 6).forEach(System.out::println);
        System.out.println();
        DoubleStream.generate(Math::random).limit(5).forEach(System.out::println);
    }
}
