import java.util.Arrays;

public class T {

    public static void main(String[] args) {
        int[] arr = new int[]{5,7,9,2,6,3,1,4,8};
        //从小到大

        for (int i = 0; i < arr.length - 1; i++) {

            for (int j = i + 1; j > 0; j--) {

                if (arr[j] > arr[j - 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = tmp;
                } else {
                    break;
                }

            }



        }

        System.out.println(Arrays.toString(arr));

    }

}
