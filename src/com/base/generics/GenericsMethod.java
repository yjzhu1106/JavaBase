package com.base.generics;

public class GenericsMethod {
    public static <E> void printArray(E[] elements){
        for(E element: elements){
            System.out.printf("%s", element);
        }
    }
}
