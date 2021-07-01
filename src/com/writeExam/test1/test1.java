package com.writeExam.test1;

import java.lang.reflect.Array;
import java.util.*;

/**
 *
 *
 */
public class test1 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
//        int[] result = new int[T];
        for(int i = 0; i < T; i++){
            int n = scanner.nextInt();
            int[][] xy = new int[n][2];
            for(int j = 0; j < n; j++){
                xy[j][0] = scanner.nextInt();
            }
            for(int j = 0; j < n; j++){
                xy[j][1] = scanner.nextInt();
            }
            Arrays.sort(xy, new Comparator<int[]>(){

                @Override
                public int compare(int[] o1, int[] o2) {
                    if(o1[0] < o2[0]){
                        return -1;
                    }else if(o1[0] > o2[0]){
                        return 1;
                    }else{
                        if(o1[1] > o2[1]){
                            return -1;
                        }else if(o1[1] < o2[1]){
                            return 1;
                        }else{
                            return -1;
                        }
                    }
                }
            });

            int[] nums = new int[n];

            for(int j = 0; j < n; j ++){
                nums[j] = xy[j][1];
            }

            int result = longestSubArray(nums);
            System.out.println(result);
        }

//        for(int t = 0; t < T; t++){
//            System.out.println(result[t]);
//        }

    }

    private static int longestSubArray(int[] nums){
        if(nums.length == 0){
            return 0;
        }
        int n = nums.length;
        int[] lon = new int[n];
        int max_lon = 0;
        lon[0] = 1;
        for(int i = 1; i < n; i++){
            lon[i] = 1;
            for(int j = (i-1)/2; j < i; j++){
                if(nums[i] > nums[j]){
                    lon[i] = Math.max(lon[i], lon[j]+1);
                }
            }
            max_lon = Math.max(max_lon, lon[i]);
        }
        return max_lon;


    }

}
