🧮 Function 1: maxAlternatingSum
function maxAlternatingSum(nums: number[]): number {
    let ans = 0n;
    nums = nums.map(Math.abs);
    nums.sort((a, b) => a - b);

    for (let i = 0; i < Math.floor(nums.length / 2); i++) {
        const high = BigInt(nums[nums.length - i - 1]);
        const low = BigInt(nums[i]);
        ans += high * high - low * low;
    }

    if (nums.length % 2 === 1) {
        const mid = BigInt(nums[Math.floor(nums.length / 2)]);
        ans += mid * mid;
    }

    return Number(ans);
}
🧠 Function 2: countStableSubarrays
📝 Concept

This counts the number of subarrays (l, r) that satisfy a certain “stable” condition (as defined in LeetCode 3728).
It maintains a prefix-sum map with a clever key pair:
(prefixSum - capacity[i], capacity[i - 1])

🔍 How it Works

Matches previous prefix patterns that could form stable subarrays.

prefixSum tracks cumulative sums.

The map efficiently counts previous states → overall O(n) performance.

⚙️ Time & Space Complexity (TLR)
Aspect	Explanation
T.C.	O(n) — single pass with constant-time map lookups
S.C.	O(n) — stores prefix sum states in the map
Approach	HashMap-based prefix sum tracking
Edge Cases	Handles zeros, negatives, and repeated elements gracefully
📘 Reference Links (for Revision)

🔗 LeetCode Problem 3728 – Count Stable Subarrays

📘 LeetCode Discuss Editorial (Prefix Sum HashMap Approach)

🔗 TypeScript Map Documentation (MDN)

🔗 Prefix Sum Concept (CP Algorithms)

✅ TypeScript Implementation
function countStableSubarrays(capacity: number[]): number {
  const n = capacity.length;
  const map = new Map<string, number>();
  let prefixSum = 0;
  let stableSubarrays = 0;

  for (let i = 0; i < n; i++) {
    prefixSum += capacity[i];

    // Step 1: Check stable condition (only if we have at least 2 previous elements)
    if (i >= 2) {
      const checkedKey = `${prefixSum - 2 * capacity[i]}|${capacity[i]}`;
      stableSubarrays += map.get(checkedKey) ?? 0;
    }

    // Step 2: Insert previous record for future checks
    if (i >= 1) {
      const insertedKey = `${prefixSum - capacity[i]}|${capacity[i - 1]}`;
      map.set(insertedKey, (map.get(insertedKey) ?? 0) + 1);
    }
  }

  return stableSubarrays;
}

🧩 Summary (TLR)

Algorithm: Prefix Sum + HashMap Pattern Matching

Time: O(n)

Space: O(n)

Core Idea: Each prefix creates a “signature” state used to detect stable subarrays efficiently.
