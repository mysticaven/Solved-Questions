#include <bits/stdc++.h>
using namespace std;


int main() 
{
    int t;cin>>t;
    while(t--){
      int n;cin>>n;
      vector<int>arr(n ,0);
      for(int i=0;i<n;i++){
        cin>>arr[i];
      }
      
      int total=0;
      
      for(int i=0;i<n;i++){
        for(int j=i;j<n;j++){
          int x=0;
          for(int k=i;k<=j;k++){
            x^=arr[k];
          }
          
          if(x==0){
            int pxor=0;int cnt=0;
            for(int k=i;k<=j;k++){
              pxor^=arr[k];
              if(pxor==0)cnt++;
            }
            total+=cnt;
          }
          
        }
      }
      
      int ans= (n*(n+1)*(n+2))/6;
      cout<<ans-total<<endl;
      
      
      
      
    }
    return 0;
}
