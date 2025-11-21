package DP;

import java.util.*;

/**
 * PROBLEM STATEMENT:
 *
 * You are given an array of size n.
 *
 * For any subset S of indices:
 * - Let T = the complement of S (indices NOT in S)
 * - sum(S) = sum of all elements at indices in S
 *
 * DEFINITION: SPECIAL/DOMINATING SUBSET
 * A subset S is called "special" or "dominating" if:
 * For every j ∈ T: sum(S) ≥ a[j]
 *
 * In plain English: The sum of chosen elements must be greater than or equal
 * to EVERY element that was NOT chosen.
 *
 * TASK: Count how many subsets S are dominating.
 *
 * KEY INSIGHT:
 * sum(S) ≥ max(T), where max(T) is the largest element among unchosen elements
 *
 * OPTIMIZED APPROACH:
 * If we choose the largest element (y) in our subset, then max(T) < y
 * So we only need: sum(S) ≥ y (the largest element in entire array)
 *
 * Using DP:
 * dp[i][j] = number of subsets from first i elements with sum = j
 * Final answer = dp[n][y] + dp[n][y+1] + ... + dp[n][totalSum]
 *               (where these subsets include the largest element)
 *
 * TIME COMPLEXITY: O(n × S) where S = sum of all elements
 * SPACE COMPLEXITY: O(n × S)
 */

public class DpSubSet {

    public static void main(String[] args) {
        int[] a = {2, 3, 1, 4};
        int n = a.length;

        solveBasicProblem(a, n);
        System.out.println();
        solveFollowUp1(a, n);
        System.out.println();
        solveFollowUp2(a, n);
    }

    /**
     * BASIC PROBLEM: Count dominating subsets
     *
     * ALGORITHM:
     * 1. Find the largest element y in the array
     * 2. Build DP table: dp[i][j] = count of subsets from first i elements with sum j
     * 3. Count subsets with sum ≥ y
     *
     * IMPORTANT: We need subsets that either:
     *   - Include the largest element AND have sum ≥ y
     *   - Include ALL elements (always valid)
     *
     * WHY THIS WORKS:
     * If sum(S) ≥ maxElement, then sum(S) ≥ any element not in S
     * because the largest possible element not in S is < maxElement
     * (assuming maxElement is in S, or if maxElement is not in S,
     *  then sum(S) ≥ maxElement handles that case too)
     */
    public static void solveBasicProblem(int[] a, int n) {
        System.out.println("=== BASIC PROBLEM: Count Dominating Subsets ===");

        // Step 1: Find the maximum element and total sum
        // maxElement (y) is our threshold - subsets must have sum ≥ y
        int maxElement = Arrays.stream(a).max().getAsInt();
        int totalSum = Arrays.stream(a).sum();

        // Step 2: Initialize DP table
        // dp[i][j] = number of ways to select elements from first i elements
        //            such that their sum equals j
        int[][] dp = new int[n + 1][totalSum + 1];

        // Base case: There's exactly 1 way to make sum 0 - select nothing
        dp[0][0] = 1;

        // Step 3: Fill DP table using subset sum logic
        for (int i = 1; i <= n; i++) {  // For each element (1-indexed)
            for (int j = 0; j <= totalSum; j++) {  // For each possible sum

                // Case 1: Don't include current element a[i-1]
                // Number of subsets = subsets from previous i-1 elements with sum j
                dp[i][j] = dp[i - 1][j];

                // Case 2: Include current element a[i-1]
                // Only possible if current sum j >= value of element a[i-1]
                if (j >= a[i - 1]) {
                    // Add subsets from previous elements with sum = j - a[i-1]
                    // Because adding a[i-1] to those subsets gives sum = j
                    dp[i][j] += dp[i - 1][j - a[i - 1]];
                }
            }
        }

        // Step 4: Count all subsets with sum >= maxElement
        // These are the dominating subsets because:
        // - If sum ≥ maxElement, then sum ≥ any unchosen element
        int count = 0;
        for (int sum = maxElement; sum <= totalSum; sum++) {
            count += dp[n][sum];  // Add count of subsets with this sum
        }

        System.out.println("Array: " + Arrays.toString(a));
        System.out.println("Max element (y): " + maxElement);
        System.out.println("Total sum: " + totalSum);
        System.out.println("Number of dominating subsets: " + count);

        // Verify with brute force for correctness
        int bruteCount = countBruteForce(a, n);
        System.out.println("Verification (brute force): " + bruteCount);
    }

    /**
     * FOLLOW-UP 1: Find the sum of all valid subsets
     *
     * EXPLANATION:
     * Instead of just counting subsets, we need to sum up their sums.
     * For example, if subsets are {1,2} and {3,4,5}:
     * - Their sums are 3 and 12
     * - Answer = 3 + 12 = 15
     *
     * APPROACH:
     * Track dpSum[i][j] = sum of subset-sums for all subsets
     *                     from first i elements with sum = j
     *
     * Example: If there are 3 subsets with sum=5, dpSum[i][5] = 5+5+5 = 15
     */
    public static void solveFollowUp1(int[] a, int n) {
        System.out.println("=== FOLLOW-UP 1: Sum of All Valid Subsets ===");

        int maxElement = Arrays.stream(a).max().getAsInt();
        int totalSum = Arrays.stream(a).sum();

        // dp[i][j] = count of subsets with sum j
        int[][] dp = new int[n + 1][totalSum + 1];

        // dpSum[i][j] = sum of all subset-sums that equal j
        // If 3 subsets have sum=5, then dpSum[i][5] = 5+5+5 = 15
        long[][] dpSum = new long[n + 1][totalSum + 1];

        // Base case: empty subset
        dp[0][0] = 1;        // 1 way to make sum 0
        dpSum[0][0] = 0;     // Empty subset sum = 0

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= totalSum; j++) {

                // Case 1: Don't take element a[i-1]
                dp[i][j] = dp[i - 1][j];
                dpSum[i][j] = dpSum[i - 1][j];

                // Case 2: Take element a[i-1]
                if (j >= a[i - 1]) {
                    int prevSum = j - a[i - 1];
                    int prevCount = dp[i - 1][prevSum];  // How many subsets had sum = prevSum

                    // Update count
                    dp[i][j] += prevCount;

                    // Update sum of sums
                    // Each of those prevCount subsets now has sum = j (after adding a[i-1])
                    // So we add j * prevCount to account for all of them
                    dpSum[i][j] += dpSum[i - 1][prevSum] + (long) j * prevCount;
                }
            }
        }

        // Calculate answer: sum of all valid subset sums (where sum >= maxElement)
        long totalValidSum = 0;
        for (int sum = maxElement; sum <= totalSum; sum++) {
            totalValidSum += dpSum[n][sum];
        }

        System.out.println("Sum of all valid subsets: " + totalValidSum);
    }

    /**
     * FOLLOW-UP 2: Find the sum of products of all valid subsets
     *
     * EXPLANATION:
     * For each valid subset, calculate product of its elements, then sum all products.
     * Example: Subsets {2,3} and {1,4,5}
     * - Products: 2×3=6 and 1×4×5=20
     * - Answer = 6 + 20 = 26
     *
     * APPROACH:
     * Track dpProd[i][j] = sum of products of all subsets
     *                      from first i elements with sum = j
     *
     * TRANSITION:
     * When we add element a[i-1] to a subset with product P:
     * New product = P × a[i-1]
     */
    public static void solveFollowUp2(int[] a, int n) {
        System.out.println("=== FOLLOW-UP 2: Sum of Products of All Valid Subsets ===");

        int maxElement = Arrays.stream(a).max().getAsInt();
        int totalSum = Arrays.stream(a).sum();

        // dp[i][j] = count of subsets with sum j
        int[][] dp = new int[n + 1][totalSum + 1];

        // dpProd[i][j] = sum of products of all subsets with sum j
        // Example: If subsets {2,3} and {1,6} both have sum=6
        // dpProd[i][6] = (2×3) + (1×6) = 6 + 6 = 12
        long[][] dpProd = new long[n + 1][totalSum + 1];

        // Base case
        dp[0][0] = 1;        // 1 empty subset
        dpProd[0][0] = 1;    // Product of empty subset = 1 (multiplicative identity)

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= totalSum; j++) {

                // Case 1: Don't take element a[i-1]
                dp[i][j] = dp[i - 1][j];
                dpProd[i][j] = dpProd[i - 1][j];

                // Case 2: Take element a[i-1]
                if (j >= a[i - 1]) {
                    int prevSum = j - a[i - 1];

                    // Update count
                    dp[i][j] += dp[i - 1][prevSum];

                    // Update product sum
                    // Each previous subset with sum=prevSum had some product P
                    // After adding a[i-1], new product = P × a[i-1]
                    // So we multiply all previous products by a[i-1]
                    dpProd[i][j] += dpProd[i - 1][prevSum] * a[i - 1];
                }
            }
        }

        // Calculate answer: sum of products of valid subsets (sum >= maxElement)
        long totalProductSum = 0;
        for (int sum = maxElement; sum <= totalSum; sum++) {
            totalProductSum += dpProd[n][sum];
        }

        System.out.println("Sum of products of all valid subsets: " + totalProductSum);

        // Note: Empty subset has product=1, but sum=0, so it won't be counted
        // since maxElement > 0 in most cases
    }

    /**
     * BRUTE FORCE VERIFICATION
     *
     * Uses bit masking to enumerate all 2^n subsets
     * For each subset:
     * - Calculate sum of chosen elements
     * - Find max of unchosen elements
     * - Check if sum >= max(unchosen)
     *
     * TIME COMPLEXITY: O(2^n × n)
     * Use only for small n (n ≤ 20) for verification
     */
    private static int countBruteForce(int[] a, int n) {
        int count = 0;

        // Iterate through all 2^n possible subsets using bit masking
        for (int mask = 0; mask < (1 << n); mask++) {
            int sum = 0;           // Sum of chosen elements
            int maxUnchosen = 0;   // Maximum of unchosen elements

            // Check each bit in the mask
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    // Bit i is set - element a[i] is chosen
                    sum += a[i];
                } else {
                    // Bit i is not set - element a[i] is not chosen
                    maxUnchosen = Math.max(maxUnchosen, a[i]);
                }
            }

            // Check if this subset is dominating
            // If all elements chosen (mask == 2^n - 1), always valid (no unchosen elements)
            // Otherwise, check if sum >= max(unchosen)
            if (mask == (1 << n) - 1 || sum >= maxUnchosen) {
                count++;
            }
        }
        return count;
    }
}