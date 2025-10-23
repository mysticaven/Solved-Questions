import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        
        while (t-- > 0) {
            int n = sc.nextInt();
            int[] arr = new int[n];
            
            for (int i = 0; i < n; i++) {
                arr[i] = sc.nextInt();
            }
            
            int total = 0;
            
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    int x = 0;
                    for (int k = i; k <= j; k++) {
                        x ^= arr[k];
                    }
                    
                    if (x == 0) {
                        int pxor = 0;
                        int cnt = 0;
                        for (int k = i; k <= j; k++) {
                            pxor ^= arr[k];
                            if (pxor == 0) cnt++;
                        }
                        total += cnt;
                    }
                }
            }
            
            int ans = (n * (n + 1) * (n + 2)) / 6;
            System.out.println(ans - total);
        }
        
        sc.close();
    }
}
