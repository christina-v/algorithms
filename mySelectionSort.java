 

import java.util.*;
import java.lang.*;

public class mySelectionSort {
 
    public static int[] doSelectionSort(int[] arr){
         
        for (int i = 0; i < arr.length - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[index]) {
                    //System.out.println(arr[j]);
                    index = j;
                }
             }     
      
            int smallerNumber = arr[index]; 
           // System.out.println(smallerNumber);
            arr[index] = arr[i];
          //  System.out.println(arr[index]);
            arr[i] = smallerNumber;
            //System.out.println(arr[i]);
        }
        return arr;
    }
     
    public static void main(String a[]){
         
        int[] arr1 = {10,34,2,56,7,67,88,42};
        int[] arr2 = doSelectionSort(arr1);
        for(int i:arr2){
            System.out.print(i);
            System.out.println(" ");
        }
    }
}
