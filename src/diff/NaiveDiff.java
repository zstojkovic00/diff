package diff;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static diff.Operation.Type.*;
import static org.assertj.core.api.Assertions.assertThat;

public class NaiveDiff implements Diff {

    @Override
    public List<Operation> diff(List<String> a, List<String> b) {
        List<Operation> result = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < a.size() && j < b.size()) {

            if (a.get(i).equals(b.get(j))) {
                result.add(new Operation(Operation.Type.EQUAL, a.get(i)));
                i++;
                j++;
            } else {
                int match = -1;
                for (int k = j; k < b.size(); k++) {
                    if (b.get(k).equals(a.get(i))) {
                        match = k;
                        break;
                    }
                }
                if (match == -1) {
                    result.add(new Operation(Operation.Type.DELETE, a.get(i)));
                    i++;
                } else {
                    for (int k = j; k < match; k++) {
                        result.add(new Operation(Operation.Type.INSERT, b.get(k)));
                    }
                    result.add(new Operation(Operation.Type.EQUAL, a.get(i)));
                    i++;
                    j = match + 1;
                }
            }
        }

        while (i < a.size()) {
            result.add(new Operation(Operation.Type.DELETE, a.get(i)));
            i++;
        }

        while (j < b.size()) {
            result.add(new Operation(Operation.Type.INSERT, b.get(j)));
            j++;
        }

        return result;
    }

    @Test
    void empty() {
        assertThat(new NaiveDiff().diff(List.of(), List.of()))
                .isEmpty();
    }

    @Test
    void identical() {
        assertThat(new NaiveDiff().diff(List.of("a", "b", "c"), List.of("a", "b", "c")))
                .containsExactly(
                        new Operation(EQUAL, "a"),
                        new Operation(EQUAL, "b"),
                        new Operation(EQUAL, "c"));
    }

    @Test
    void different() {
        assertThat(new NaiveDiff().diff(List.of("a", "b"), List.of("c", "d")))
                .containsExactly(
                        new Operation(DELETE, "a"),
                        new Operation(DELETE, "b"),
                        new Operation(INSERT, "c"),
                        new Operation(INSERT, "d"));
    }

    @Test
    void naiveChapterExample() {
        assertThat(new NaiveDiff().diff(List.of("p", "q", "p"), List.of("q", "p")))
                .containsExactly(
                        new Operation(INSERT, "q"),
                        new Operation(EQUAL, "p"),
                        new Operation(DELETE, "q"),
                        new Operation(DELETE, "p"));
    }

}
