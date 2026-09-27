package diff;

import java.util.ArrayList;
import java.util.List;

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
}
