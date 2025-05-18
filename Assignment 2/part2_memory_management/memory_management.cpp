#include <iostream>
using namespace std;

int main() {
    int* arr = new int[5]{1, 2, 3, 4, 5};
    int sum = 0;
    for (int i = 0; i < 5; ++i) {
        sum += arr[i];
    }
    cout << "Sum: " << sum << endl;
    delete[] arr;
    return 0;
}
