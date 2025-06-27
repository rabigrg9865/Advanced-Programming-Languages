class StatisticsCalculator:
    def __init__(self, numbers):
        self.numbers = numbers

    def mean(self):
        return sum(self.numbers) / len(self.numbers)

    def median(self):
        sorted_nums = sorted(self.numbers)
        n = len(sorted_nums)
        mid = n // 2
        if n % 2 == 0:
            return (sorted_nums[mid - 1] + sorted_nums[mid]) / 2
        else:
            return sorted_nums[mid]

    def mode(self):
        freq = {}
        for num in self.numbers:
            freq[num] = freq.get(num, 0) + 1
        max_count = max(freq.values())
        for num, count in freq.items():
            if count == max_count:
                return num  # Return first mode found

# Example usage
if __name__ == "__main__":
    numbers = [2, 3, 5, 3, 8, 3, 10]
    calc = StatisticsCalculator(numbers)

    print(f"Mean: {calc.mean():.2f}")
    print(f"Median: {calc.median():.2f}")
    print(f"Mode: {calc.mode()}")