package streams;

import java.util.stream.IntStream;

public class PrimitiveStreams {
    public static void main(String[] args) {
        IntStream.range(1, 6).forEach(System.out::println); // 1 to 5
    }
}