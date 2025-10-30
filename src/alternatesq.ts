function maxAlternatingSum(nums: number[]): number {
  const n = nums.length;

  // Step 1: square each element
  for (let i = 0; i < n; i++) {
    nums[i] = nums[i] * nums[i];
  }

  // Step 2: sort descending
  nums.sort((a, b) => b - a);

  // Step 3: determine midpoint
  let d: number;
  if (n % 2 === 0) {
    d = n / 2 - 1;
  } else {
    d = Math.floor(n / 2);
  }

  // Step 4: compute alternating sum thats problem done mf****
  let ans = 0;
  for (let i = 0; i < n; i++) {
    if (i <= d) {
      ans += nums[i];
    } else {
      ans -= nums[i];
    }
  }

  return ans;
}
