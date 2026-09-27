package diff;

import java.util.List;

public interface Diff {

    List<Operation> diff(List<String> a, List<String> b);
}
