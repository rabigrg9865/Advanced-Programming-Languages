# Type system example
x = 5
x = "Hello"
print(x)

# Closure example
def outer():
    x = "outer value"
    def inner():
        print("Accessing:", x)
    return inner

f = outer()
f()
