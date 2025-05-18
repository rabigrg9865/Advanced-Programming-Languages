#include <iostream>
#include <functional>
#include <string>
using namespace std;

int main() {
    string x = "Hello";
    cout << x << endl;

    function<void()> outer() {
        string x = "outer value";
        return [x]() { cout << "Accessing: " << x << endl; };
    }

    auto f = outer();
    f();

    return 0;
}
