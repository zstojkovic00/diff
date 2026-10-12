package diff;

import diff.Operation.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static diff.Operation.Type.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MyersDiff implements Diff {

    @Override
    public List<Operation> diff(List<String> a, List<String> b) {
        List<int[]> trace = shortestEditScript(a, b);
        return backtrack(a, b, trace);
    }

    private List<Operation> backtrack(List<String> a,
                                      List<String> b,
                                      List<int[]> trace) {
        int d = trace.size() - 1;
        int x = a.size();
        int y = b.size();
        int offset = x + y;
        List<Operation> operations = new ArrayList<>();

        for (int dd = d; dd >= 0; --dd) {
            int[] v = trace.get(dd);
            int k = x - y;

            int pk;
            if (k == -dd) {
                pk = k + 1;
            } else if (k == dd) {
                pk = k - 1;
            } else {
                if (v[offset + k - 1] < v[offset + k + 1]) {
                    pk = k + 1;
                } else {
                    pk = k - 1;
                }
            }

            int px = v[offset + pk];
            int py = px - pk;

            while (x > px && y > py) {
                operations.add(new Operation(Type.EQUAL, a.get(x - 1)));
                x--;
                y--;
            }

            if (dd > 0) {
                if (x == px) {
                    operations.add(new Operation(Type.INSERT, b.get(y - 1)));
                } else {
                    operations.add(new Operation(Type.DELETE, a.get(x - 1)));
                }
            }

            x = px;
            y = py;
        }

        Collections.reverse(operations);
        return operations;
    }

    private List<int[]> shortestEditScript(List<String> a,
                                           List<String> b) {
        int n = a.size();
        int m = b.size();
        int max = n + m;
        int offset = max;
        int[] v = new int[max * 2 + 2];
        List<int[]> trace = new ArrayList<>();

        for (int d = 0; d <= max; d++) {
            trace.add(v.clone());
            for (int k = -d; k <= d; k += 2) {
                int x;
                if (k == -d) {
                    x = v[offset + k + 1];
                } else if (k == d) {
                    x = v[offset + k - 1] + 1;
                } else {
                    if (v[offset + k - 1] < v[offset + k + 1]) {
                        x = v[offset + k + 1];
                    } else {
                        x = v[offset + k - 1] + 1;
                    }
                }
                int y = x - k;
                while (x < a.size() && y < b.size() && a.get(x).equals(b.get(y))) {
                    x++;
                    y++;
                }
                v[offset + k] = x;

                if (x >= n && y >= m) {
                    return trace;
                }
            }
        }
        return trace;
    }

    @Test
    void empty() {
        assertThat(new MyersDiff().diff(List.of(), List.of()))
                .isEmpty();
    }

    @Test
    void identical() {
        assertThat(new MyersDiff().diff(List.of("a", "b", "c"), List.of("a", "b", "c")))
                .containsExactly(
                        new Operation(EQUAL, "a"),
                        new Operation(EQUAL, "b"),
                        new Operation(EQUAL, "c"));
    }

    @Test
    void different() {
        assertThat(new MyersDiff().diff(List.of("a", "b"), List.of("c", "d")))
                .containsExactly(
                        new Operation(DELETE, "a"),
                        new Operation(DELETE, "b"),
                        new Operation(INSERT, "c"),
                        new Operation(INSERT, "d"));
    }
    @Test
    void myersPaperExample() {
        assertThat(new MyersDiff().diff(
                List.of("a", "b", "c", "a", "b", "b", "a"),
                List.of("c", "b", "a", "b", "a", "c")))
                .containsExactly(
                        new Operation(DELETE, "a"),
                        new Operation(DELETE, "b"),
                        new Operation(EQUAL, "c"),
                        new Operation(INSERT, "b"),
                        new Operation(EQUAL, "a"),
                        new Operation(EQUAL, "b"),
                        new Operation(DELETE, "b"),
                        new Operation(EQUAL, "a"),
                        new Operation(INSERT, "c"));
    }

}
