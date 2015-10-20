/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/6/15
 */

public class ArraySort {

    //归并排序
    public static  int[] sort(int[]a,int[]b){
        int left=a.length;
        int right=b.length;

        int[]c=new int[left+right];

        int i,j,k;
        i=j=k=0;
        while(i<left && j<right){
            if(a[i]<b[j]){
                c[k++]=a[i++];
            }else{
                c[k++]=b[j++];

            }
        }
        while(i<left){
            c[k++]=a[i++];
        }
        while(j<right){
            c[k++]=b[j++];
        }

        return c;
    }
    public static  int[] mergesort(int []data){
        return mergesort(data,0,data.length-1);
    }
    public  static  int[] mergesort(int[] data,int start,int end){
            if(start >=end){
                return null;
            }
            int center=(start+end)/2;
            mergesort(data,start,center);
            mergesort(data, start + 1, end);

            return data;

    }



    //冒泡排序
    public static  int[] maopao(int []a){
        int n=a.length;
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                if(a[i]>a[j]){
                    int temp=a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
            }
        }
        return a;

    }


    //快速排序
    public static  int[] fastSort(int []a,int start ,int end){

        if(start < end) {
            int base=a[start];
            int low=start;
            int hight=end;
            while (low < hight) {
                while (low < hight && a[hight] > base) {
                    hight--;
                }
                a[low] = a[hight];
                while (low < hight && a[low] < base) {
                    low++;
                }
                a[hight] = a[low];

            }
            a[low] = base;
            fastSort(a, start, low - 1);
            fastSort(a, low + 1, end);
        }
        return a;


    }

    //插入排序
    public static int[] insertSort(int[]a){
        int i, j;
        int n = a.length;
        int target;

//        //假定第一个元素被放到了正确的位置上
//        //这样，仅需遍历1 - n-1
//        for (i = 1; i < n; i++)
//        {
//            j = i;
//            target = a[i];
//
//            while (j > 0 && target < a[j - 1])
//            {
//                a[j] = a[j - 1];
//                j--;
//            }
//
//            a[j] = target;
//        }

        for(i=1;i<n;i++){
            for(j=i-1;j>=0 && a[j]>a[j+1];j--){
                int temp=a[j];
                a[j]=a[j+1];
                a[j+1]=temp;
            }

        }
        return a;
    }

    public static void main(String[] args) {
        int[] a={16,88,2,32,66,97,3,99,9};
        int[] b={11,13};
        int[]c = mergesort(a);
        for(int i:c){
            System.out.println(i);
        }
        System.out.println(1/2);
    }
}
