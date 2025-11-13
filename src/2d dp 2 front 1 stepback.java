import java.util.*;

class SubarrayAvg {
    // Returns minimum sum to exit the array using +2 forward jumps and at most one immediate -1 backward after a forward
    public static long minCostOneBackJump(int[] B) {
        int n = B.length;
        if (n == 0) return 0L; // no cost to exit
        final long INF = Long.MAX_VALUE / 4;

        long[] dpF = new long[n]; // arrive at i via forward (+2) from i-2
        long[] dpB = new long[n]; // arrive at i via backward (-1) from i+1 right after a forward

        Arrays.fill(dpF, INF);
        Arrays.fill(dpB, INF);

        // Base cases
        dpF[0] = B[0];
        if (n >= 2) {
            // dpB[0] impossible; dpF[1] impossible by +2; dpB[1] may be computed below when i=1 if n>2
        }

        // i = 1 special handling for backward arrival from 2 -> 1 (needs n >= 3)
        if (n >= 3) {
            dpB[1] = (long) B[1] + B[2] + dpF[0];
        }

        // Fill DP
        for (int i = 2; i < n; i++) {
            // forward arrival to i from i-2
            dpF[i] = (long) B[i] + Math.min(dpF[i - 2], dpB[i - 2]);

            // backward arrival to i from i+1 (only if i+1 < n), requiring dpF[i-1] to have arrived at i+1 via forward from i-1
            if (i + 1 < n) {
                dpB[i] = (long) B[i] + B[i + 1] + dpF[i - 1];
            }
        }

        // Compute answer: min over states that can jump out by +2
        long ans;
        if (n == 1) {
            ans = dpF[0];
        } else {
            ans = Math.min(
                    Math.min(dpF[n - 2], dpB[n - 2]),
                    Math.min(dpF[n - 1], dpB[n - 1])
            );
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(minCostOneBackJump(new int[]{2, 10, 8, -5, -10, 5}));
        System.out.println(minCostOneBackJump(new int[]{2, -100, 8, 5, 0}));
    }
}