#include <stdio.h>
#include <stdlib.h>

// Function to compare integers for qsort
int compare(const void *a, const void *b) {
    return (*(int *)a - *(int *)b);
}

// Function to calculate mean
float calculate_mean(int arr[], int n) {
    int sum = 0;
    for (int i = 0; i < n; i++)
        sum += arr[i];
    return (float)sum / n;
}

// Function to calculate median
float calculate_median(int arr[], int n) {
    qsort(arr, n, sizeof(int), compare);
    if (n % 2 == 0)
        return (arr[n / 2 - 1] + arr[n / 2]) / 2.0;
    else
        return arr[n / 2];
}

// Function to calculate mode (returns first mode found if multiple)
int calculate_mode(int arr[], int n) {
    int maxValue = 0, maxCount = 0;
    for (int i = 0; i < n; ++i) {
        int count = 0;
        for (int j = 0; j < n; ++j) {
            if (arr[j] == arr[i])
                ++count;
        }
        if (count > maxCount) {
            maxCount = count;
            maxValue = arr[i];
        }
    }
    return maxValue;
}

int main() {
    int numbers[] = {2, 3, 5, 3, 8, 3, 10};
    int n = sizeof(numbers) / sizeof(numbers[0]);

    float mean = calculate_mean(numbers, n);
    float median = calculate_median(numbers, n);
    int mode = calculate_mode(numbers, n);

    printf("Mean: %.2f\n", mean);
    printf("Median: %.2f\n", median);
    printf("Mode: %d\n", mode);

    return 0;
}