import java.util.*;

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

class array4{
    public static void main(String[] args) {

        int arr[] = {5, 8, 2, 10, 3};

        int largest = arr[0];   // assume first element is largest

        for(int i = 1; i < arr.length; i++) {
            if(arr[i] > largest) {
                largest = arr[i];   // update largest
            }
        }

        System.out.println("Largest number = " + largest);
    }
}

class array5 {
    public static void main(String[] args) {

        int arr[] = {1, 2, 4, 7, 7};

        int largest = arr[0];

        // Step 1: find largest
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > largest) {
                largest = arr[i];
            }
        }

        int secondLargest = Integer.MIN_VALUE;

        // Step 2: find second largest
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > secondLargest && arr[i] != largest) {
                secondLargest = arr[i];
            }
        }

        System.out.println("Largest = " + largest);
        System.out.println("Second Largest = " + secondLargest);
    }
}

class array6{
    static void main(String[] args) {
        int arr[] = {10,19,20,30,17,24,6};
        int largest = arr[0];
        for(int i = 0;i < arr.length-1;i++){
            if(arr[i] > largest){
                largest = arr[i];
            }
        }
        System.out.println("The largest number is :" + largest);
    }
}

class array7{
    static void main(String[] args) {
        int arr[] ={1,2,3,4,5,6};
        int largest = arr[0];
        for(int i=0;i<arr.length;i++){
            if(arr[i]>largest){
                largest = arr[i];
            }
        }
        int secondlargest = -1;
        for(int i =0;i<arr.length;i++){
            if(arr[i] > secondlargest && arr[i] < largest){
                secondlargest = arr[i];
            }
        }
        System.out.println("The second largest number is :" + secondlargest);
    }
}

class array8 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};

        for (int i = 1; i < arr.length; i++) {
            //if (arr[i] > arr[i - 1])
              if(arr[i] < arr[i-1]){
                System.out.println("sorted");
            }
        }
        System.out.println("unsorted");
    }
}

class array9{
    public static void main(String[] args) {
        int[] arr = {4, 2, 4, 5, 2, 7};
        System.out.println(removeDuplicates(arr));
        // Output: [4, 2, 5, 7]
    }
    public static List<Integer> removeDuplicates(int[] arr) {
        HashSet<Integer> seen = new HashSet<>();   // remembers what we've already added
        ArrayList<Integer> result = new ArrayList<>();  // holds our final unique elements
        for (int num : arr) {
            if (!seen.contains(num)) {   // if we've NEVER seen this number before
                seen.add(num);           // mark it as seen
                result.add(num);         // keep it in our answer
            }
            // if seen.contains(num) is true, we do nothing — it's a duplicate, skip it
        }
        return result;
    }
}


//        Remove duplicates → return all unique values (each value once), keeping first occurrence.
//        Find duplicates → return only values that occur 2+ times — the unique ones (appearing once) get excluded.

//class Solution {
//    public ArrayList<Integer> findDuplicates(int[] arr) {
//        int maxVal = 0;
//        for (int num : arr) {
//            maxVal = Math.max(maxVal, num);
//        }
//
//        int[] count = new int[maxVal + 1];   // count occurrences, not just boolean
//        for (int num : arr) {
//            count[num]++;
//        }
//
//        ArrayList<Integer> result = new ArrayList<>();
//        for (int num = 0; num <= maxVal; num++) {
//            if (count[num] >= 2) {           // appeared more than once
//                result.add(num);
//            }
//        }
//        return result;
//    }
//}

class array10 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int temp = arr[0];  // Step 1: save the first element before it gets overwritten
        // Step 2: shift every element one position to the left
        for (int i = 1; i < arr.length; i++) {
            arr[i - 1] = arr[i];
        }
        // Step 3: place the saved first element at the very end
        arr[arr.length - 1] = temp;
        // print result to verify
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }
}



