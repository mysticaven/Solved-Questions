import java.util.*;

public class BalanceSubstringRemover {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt(); // Number of test cases

        while (t-- > 0) {
            int n = sc.nextInt();
            String s = sc.next();

            int ta = 0, tb = 0;
            int[] prefixDiff = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                char c = s.charAt(i - 1);
                if (c == 'a') {
                    ta++;
                    prefixDiff[i] = prefixDiff[i - 1] + 1;
                } else {
                    tb++;
                    prefixDiff[i] = prefixDiff[i - 1] - 1;
                }
            }

            int k = ta - tb;
            if (k == 0) {
                System.out.println(0);
                continue;
            }

            Map<Integer, Integer> map = new HashMap<>();
            map.put(0, 0);
            int ans = n;

            for (int i = 1; i <= n; i++) {
                int need = prefixDiff[i] - k;
                if (map.containsKey(need)) {
                    int len = i - map.get(need);
                    ans = Math.min(ans, len);
                }
                map.putIfAbsent(prefixDiff[i], i);
            }

            System.out.println(ans == n ? -1 : ans);
        }
    }
}