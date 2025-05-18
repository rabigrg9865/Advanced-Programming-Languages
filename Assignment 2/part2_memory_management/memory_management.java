public class MemoryDemo {
    public static void main(String[] args) {
        int[] data = new int[]{1, 2, 3, 4, 5};
        System.out.println("Sum: " + calculateSum(data));
    }

    public static int calculateSum(int[] arr) {
        int total = 0;
        for (int num : arr) {
            total += num;
        }
        return total;
    }
}
