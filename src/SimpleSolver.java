
//# Maximum Bitwise AND Subset (with at most K increments)
//
//        ## Problem Statement
//
//        You are given an array `A` of `N` positive integers.
//        You have to choose **exactly `M`** elements and you may perform the operation **at most `K`** times in total:
//
//        - **Operation**: Choose any index `i` and increment `A[i]` by 1.
//
//        Find the maximum possible bitwise AND of the `M` chosen elements after performing the operation ≤ `K` times.
//
//        ### Constraints
//        - `1 ≤ M ≤ N ≤ 10⁵`
//        - `1 ≤ K ≤ 10⁹`
//        - `1 ≤ A[i] ≤ 10⁹`
//
//        ## Intuition
//
//        The bitwise AND of a subset has a `1` in a bit position only if **all** selected numbers have `1` there.
//        Higher bits are far more valuable (2³⁰ >> 2⁰), so we should greedily try to set the highest possible bits to `1`.
//
//        We iterate from the most significant bit (bit 30) down to bit 0 and decide for each bit whether we can force at least `M` numbers to have that bit set using the remaining budget `K`.
//
//        ## Key Observations
//
//        - To force a number `x` to have bit `b` set, we need to increment it until the next multiple of `2^{b+1}` that has bit `b` set, i.e., cost = `2^b - (x mod 2^b)`.
//        - After paying this cost, all lower bits of that number become `0` for future decisions (because we effectively rounded it up).
import java.util.*;

public class SimpleSolver {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Input Reading
        int n = sc.nextInt();
        int m = sc.nextInt();
        long k = sc.nextLong(); // Budget K

        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextLong();
        }

        // Initially, all indices are candidates
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            candidates.add(i);
        }

        long finalAns = 0;

        // 2. Iterate from Bit 30 down to 0
        for (int i = 30; i >= 0; i--) {
            long bitVal = 1L << i;

            ArrayList<Integer> hasBit = new ArrayList<>();
            ArrayList<Integer> noBit = new ArrayList<>();

            // Separate candidates into two groups
            for (int index : candidates) {
                // Check if the bit is already set
                if ((arr[index] & bitVal) != 0) {
                    hasBit.add(index);
                } else {
                    noBit.add(index);
                }
            }

            // 3. Logic to decide if we take this bit
            if (hasBit.size() >= m) {
                // Case A: We already have enough numbers with this bit.
                // We don't need to spend K. We filter candidates to keep only these.
                finalAns |= bitVal;
                candidates = hasBit;
            } else {
                // Case B: We need more numbers. Calculate cost to "buy" this bit.
                int needed = m - hasBit.size();

                // Calculate cost for numbers that DON'T have the bit
                ArrayList<CostPair> costs = new ArrayList<>();
                for (int index : noBit) {
                    // Cost = Target - (Current Lower Bits)
                    // Because adding this cost clears lower bits and sets the current bit
                    long currentLowerBits = arr[index] & (bitVal - 1);
                    long cost = bitVal - currentLowerBits;
                    costs.add(new CostPair(cost, index));
                }

                // Sort by cheapest cost
                Collections.sort(costs, (a, b) -> Long.compare(a.cost, b.cost));

                // Check if we can afford 'needed' amount
                if (costs.size() >= needed) {
                    long totalCost = 0;
                    for (int j = 0; j < needed; j++) {
                        totalCost += costs.get(j).cost;
                    }

                    if (totalCost <= k) {
                        // We can afford it!
                        k -= totalCost;
                        finalAns |= bitVal;

                        // Update candidates
                        ArrayList<Integer> nextCandidates = new ArrayList<>(hasBit);

                        for (int j = 0; j < needed; j++) {
                            CostPair p = costs.get(j);
                            nextCandidates.add(p.index);
                            // IMPORTANT: When we increment a number to force a bit,
                            // the operation clears all lower bits (they become 0).
                            arr[p.index] = 0;
                        }
                        candidates = nextCandidates;
                    }
                    // Else: If we can't afford it, we skip this bit
                    // and keep the previous 'candidates' list for the next iteration.
                }
            }
        }

        System.out.println(finalAns);
    }

    // Helper class to store cost and index together for sorting
    static class CostPair {
        long cost;
        int index;

        public CostPair(long cost, int index) {
            this.cost = cost;
            this.index = index;
        }
    }
}