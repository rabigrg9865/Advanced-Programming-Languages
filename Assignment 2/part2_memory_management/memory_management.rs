fn main() {
    let data = vec![1, 2, 3, 4, 5]; // memory is allocated on the heap
    print_sum(&data); // borrowing (no ownership transfer)
    println!("Original vector: {:?}", data);
}

fn print_sum(numbers: &Vec<i32>) {
    let sum: i32 = numbers.iter().sum();
    println!("Sum: {}", sum);
}
