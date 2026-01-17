import java.util.Arrays;
import java.util.Scanner;
//single dimension array
//basic program
class arrays1{
    static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] arr = new int[5];
        arr[0] = 23;
        arr[1] = 45;
        arr[2] = 13;
        System.out.println(arr[1]);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = in.nextInt();
            //System.out.print(arr[i]);
        }
        //for(int num:arr){
        //System.out.println(num);
        // }
        System.out.println(Arrays.toString(arr));
    }
}

//string
class arrays2{
    static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] str = new String[4];
        for(int i=0;i< str.length;i++){
            str[i] = in.next();
        }
        System.out.println(Arrays.toString(str));
        str[1] = "shivv";
        System.out.println(Arrays.toString(str));
    }
}

//passing function in arrays
class arrays3{
    static void main(String[] args) {
        int[] nums = {2, 3, 4, 5, 6};
        System.out.println(Arrays.toString(nums));
        change(nums);
        System.out.println(Arrays.toString(nums));
    }
    static void change(int[] arr) {
        arr[0] = 99;
    }
}

//multidimensional array