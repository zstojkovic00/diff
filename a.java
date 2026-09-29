import diff.Diff;
import diff.NaiveDiff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> a = Files.readAllLines(Path.of("a.java"));
        List<String> b = Files.readAllLines(Path.of("b.java"));

        List<Diff> algorithms = List.of(new NaiveDiff());

        for (Diff algorithm : algorithms) {
            algorithm.diff(a, b).forEach(System.out::println);
        }
    }
}
