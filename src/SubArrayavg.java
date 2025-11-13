import java.util.*;
public class SubArrayavg {

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int arraySize = sc.nextInt();
            int targetAverage = sc.nextInt();
            int[] elements = new int[arraySize];

            for (int i = 0; i < arraySize; i++) {
                elements[i] = sc.nextInt();
            }

            // Compute prefix sums
            long[] prefixSum = new long[arraySize + 1];
            for (int i = 1; i <= arraySize; i++) {
                prefixSum[i] = prefixSum[i - 1] + elements[i - 1];
            }

            // Map to store frequency of transformed prefix sums
            TreeMap<Long, Integer> transformedPrefixFrequency = new TreeMap<>();
            transformedPrefixFrequency.put(0L, 1); // Base case: prefixSum[0] - targetAverage * 0

            long subarrayCount = 0;

            for (int endIndex = 1; endIndex <= arraySize; endIndex++) {
                long currentTransformed = prefixSum[endIndex] - (long) targetAverage * endIndex;

                // Count how many previous transformed values are <= current
                subarrayCount += countLessOrEqual(transformedPrefixFrequency, currentTransformed);

                transformedPrefixFrequency.put(currentTransformed,
                        transformedPrefixFrequency.getOrDefault(currentTransformed, 0) + 1);
            }

            System.out.println(subarrayCount);
        }

        private static long countLessOrEqual(TreeMap<Long, Integer> map, long value) {
            long total = 0;
            for (var entry : map.headMap(value + 1).entrySet()) {
                total += entry.getValue();
            }
            return total;
        }
    }
//```
//
//        ## **Explanation**
//
//    This program counts the number of **subarrays with average ≥ K**.
//
//            ### **Algorithm Overview:**
//
//    The key insight is that a subarray from index `i` to `j` has average ≥ K if:
//            ```
//            (sum[i..j]) / (j - i + 1) ≥ K
//```
//
//    Rearranging:
//            ```
//    sum[i..j] ≥ K * (j - i + 1)
//    prefixSum[j] - prefixSum[i-1] ≥ K * (j - i + 1)
//    prefixSum[j] - K*j ≥ prefixSum[i-1] - K*(i-1)

