// Code by Pronay Debnath
// Created:- 1/10/2023
// File Name: RecursiveBinarySearch.java
// Origem: https://github.com/TheAlgorithms/Java
// Explanation:- https://www.tutorialspoint.com/java-program-for-binary-search-recursive
package exercicio5.algorithms;

// Create a SearchAlgorithm class with a generic type
abstract class SearchAlgorithm<T extends Comparable<T>> {
    // Abstract find method to be implemented by subclasses
    public abstract int find(T[] arr, T target);
}

public class RecursiveBinarySearch<T extends Comparable<T>> extends SearchAlgorithm<T> {

    // Override the find method as required
    @Override
    public int find(T[] arr, T target) {
        // Handle empty array
        if (arr == null || arr.length == 0) {
            return -1;
        }
        // Call the recursive binary search function
        return binsear(arr, 0, arr.length - 1, target);
    }

    // Recursive binary search function
    public int binsear(T[] arr, int left, int right, T target) {
        if (right >= left) {
            int mid = left + (right - left) / 2;

            // Compare the element at the middle with the target
            int comparison = arr[mid].compareTo(target);

            // If the element is equal to the target, return its index
            if (comparison == 0) {
                return mid;
            }

            // If the element is greater than the target, search in the left subarray
            if (comparison > 0) {
                return binsear(arr, left, mid - 1, target);
            }

            // Otherwise, search in the right subarray
            return binsear(arr, mid + 1, right, target);
        }

        // Element is not present in the array
        return -1;
    }
}

