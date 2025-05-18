// Type system example
let x = 5;
x = "Hello";
console.log(x);

// Closure example
function outer() {
    let x = "outer value";
    function inner() {
        console.log("Accessing:", x);
    }
    return inner;
}

const f = outer();
f();
