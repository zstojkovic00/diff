package diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DpDiff implements Diff {

    @Override
    public List<Operation> diff(List<String> a, List<String> b) {
        int[][] dp = longestCommonSubsequenceTable(a, b);
        return traceback(a, b, dp);
    }

    private int[][] longestCommonSubsequenceTable(List<String> a, List<String> b) {
        int n = a.size();
        int m = b.size();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.get(i - 1).equals(b.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp;
    }

    private List<Operation> traceback(List<String> a, List<String> b, int[][] dp) {
        List<Operation> result = new ArrayList<>();
        int i = a.size();
        int j = b.size();

        while (i > 0 && j > 0) {
            if (a.get(i - 1).equals(b.get(j - 1))) {
                result.add(new Operation(Operation.Type.EQUAL, a.get(i - 1)));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                result.add(new Operation(Operation.Type.DELETE, a.get(i - 1)));
                i--;
            } else {
                result.add(new Operation(Operation.Type.INSERT, b.get(j - 1)));
                j--;
            }
        }

        while (i > 0) {
            result.add(new Operation(Operation.Type.DELETE, a.get(i - 1)));
            i--;
        }

        while (j > 0) {
            result.add(new Operation(Operation.Type.INSERT, b.get(j - 1)));
            j--;
        }

        Collections.reverse(result);
        return result;
    }
}
