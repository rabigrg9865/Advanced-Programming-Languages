import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class ShiftScheduler {
    static final int MAX_WORK_DAYS = 5;
    static final int MIN_EMPLOYEES_PER_SHIFT = 2;
    static final String[] SHIFTS = {"morning", "afternoon", "evening"};
    static final String[] DAYS_OF_WEEK = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    static class Employee {
        String name;
        Map<String, Map<String, Integer>> preferences = new HashMap<>();
        int assignedDays = 0;

        Employee(String name) {
            this.name = name;
        }

        void inputPreferences(Scanner scanner) {
            for (String day : DAYS_OF_WEEK) {
                Map<String, Integer> dailyPrefs = new HashMap<>();
                while (true) {
                    System.out.print("Enter preferred shift order for " + name + " on " + day + " (e.g., morning afternoon evening): ");
                    String[] input = scanner.nextLine().toLowerCase().trim().split("\\s+");
                    if (isValidShiftOrder(input)) {
                        for (int i = 0; i < input.length; i++) {
                            dailyPrefs.put(input[i], i + 1);
                        }
                        break;
                    } else {
                        System.out.println("⚠️ Invalid input. Please enter all 3 shifts in any order: morning, afternoon, evening.");
                    }
                }
                preferences.put(day, dailyPrefs);
            }
        }

        private boolean isValidShiftOrder(String[] input) {
            if (input.length != 3) return false;
            Set<String> unique = new HashSet<>(Arrays.asList(input));
            return unique.containsAll(Arrays.asList(SHIFTS));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Employee> employees = generateEmployees(scanner);
        Map<String, Map<String, List<String>>> schedule = scheduleEmployees(employees);
        printSchedule(schedule);
        exportScheduleToCSV(schedule, "weekly_schedule.csv");
        scanner.close();
    }

    private static List<Employee> generateEmployees(Scanner scanner) {
        List<Employee> employees = new ArrayList<>();
        System.out.println("Enter employee names (type 'done' to finish):");
        while (true) {
            System.out.print("Employee name: ");
            String name = scanner.nextLine().trim();
            if (name.equalsIgnoreCase("done")) break;
            if (!name.isEmpty()) {
                Employee emp = new Employee(name);
                emp.inputPreferences(scanner);
                employees.add(emp);
            }
        }
        return employees;
    }

    private static Map<String, Map<String, List<String>>> scheduleEmployees(List<Employee> employees) {
        Map<String, Map<String, List<String>>> schedule = new LinkedHashMap<>();
        Map<String, Map<String, String>> employeeAssignments = new HashMap<>();
        Map<String, Integer> workDays = new HashMap<>();

        for (Employee emp : employees) {
            workDays.put(emp.name, 0);
            Map<String, String> dayMap = new HashMap<>();
            for (String day : DAYS_OF_WEEK) {
                dayMap.put(day, null);
            }
            employeeAssignments.put(emp.name, dayMap);
        }

        for (String day : DAYS_OF_WEEK) {
            schedule.put(day, new LinkedHashMap<>());
            for (String shift : SHIFTS) {
                schedule.get(day).put(shift, new ArrayList<>());
            }

            Collections.shuffle(employees);
            for (String shift : SHIFTS) {
                List<String[]> eligible = new ArrayList<>();

                for (Employee emp : employees) {
                    if (workDays.get(emp.name) < MAX_WORK_DAYS &&
                        employeeAssignments.get(emp.name).get(day) == null &&
                        emp.preferences.get(day).get(shift) == 1) {
                        eligible.add(new String[]{emp.name, "1"});
                    }
                }

                if (eligible.size() < MIN_EMPLOYEES_PER_SHIFT) {
                    for (Employee emp : employees) {
                        if (workDays.get(emp.name) < MAX_WORK_DAYS &&
                            employeeAssignments.get(emp.name).get(day) == null &&
                            eligible.stream().noneMatch(e -> e[0].equals(emp.name))) {
                            int rank = emp.preferences.get(day).get(shift);
                            eligible.add(new String[]{emp.name, String.valueOf(rank)});
                            if (eligible.size() >= MIN_EMPLOYEES_PER_SHIFT) break;
                        }
                    }
                }

                for (int i = 0; i < Math.min(MIN_EMPLOYEES_PER_SHIFT, eligible.size()); i++) {
                    String name = eligible.get(i)[0];
                    schedule.get(day).get(shift).add(name);
                    workDays.put(name, workDays.get(name) + 1);
                    employeeAssignments.get(name).put(day, shift);
                }
            }
        }
        return schedule;
    }

    private static void printSchedule(Map<String, Map<String, List<String>>> schedule) {
        System.out.println("\n🗓️  Weekly Employee Shift Schedule");
        System.out.println("========================================");
        for (String day : DAYS_OF_WEEK) {
            System.out.println("\n" + day);
            for (String shift : SHIFTS) {
                List<String> assigned = schedule.get(day).get(shift);
                String shiftName = shift.substring(0, 1).toUpperCase() + shift.substring(1);
                System.out.println("  " + shiftName + ": " + (assigned.isEmpty() ? "No one assigned" : String.join(", ", assigned)));
            }
        }
    }

    private static void exportScheduleToCSV(Map<String, Map<String, List<String>>> schedule, String filename) {
        try (FileWriter csvWriter = new FileWriter(filename)) {
            csvWriter.write("Day,Shift,Employees\n");
            for (String day : DAYS_OF_WEEK) {
                for (String shift : SHIFTS) {
                    List<String> employees = schedule.get(day).get(shift);
                    csvWriter.write(day + "," + shift + "," + String.join(" ", employees) + "\n");
                }
            }
            System.out.println("\n✅ Schedule exported to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the CSV file.");
        }
    }
}