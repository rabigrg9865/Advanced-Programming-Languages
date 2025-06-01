import random
import csv
from collections import defaultdict

# Constants
MAX_WORK_DAYS = 5
MIN_EMPLOYEES_PER_SHIFT = 2
SHIFTS = ["morning", "afternoon", "evening"]
DAYS_OF_WEEK = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]

# --- Prompt User for Employee Names and Preferences ---
def get_employee_names():
    names = []
    print("Enter employee names (type 'done' to finish):")
    while True:
        name = input("Employee name: ").strip()
        if name.lower() == "done":
            break
        elif name:
            names.append(name)
    return names

def get_preferences_for_day(day):
    preferences = {}
    print(f"\nEnter preferred shift order for {day} (e.g., morning afternoon evening):")
    while True:
        prefs = input(f"Preference order for {day}: ").lower().strip().split()
        if sorted(prefs) == sorted(SHIFTS):
            for i, shift in enumerate(prefs):
                preferences[shift] = i + 1
            return preferences
        else:
            print(f"⚠️ Please enter all shifts in some order: {', '.join(SHIFTS)}")

def generate_employees():
    employees = []
    employee_names = get_employee_names()
    for name in employee_names:
        print(f"\n--- Preferences for {name} ---")
        weekly_preferences = {day: get_preferences_for_day(day) for day in DAYS_OF_WEEK}
        employees.append({
            "name": name,
            "preferences": weekly_preferences,
            "assigned_days": 0
        })
    return employees

# --- Schedule Employees Based on Preferences ---
def schedule_employees(employees):
    schedule = {day: {shift: [] for shift in SHIFTS} for day in DAYS_OF_WEEK}
    employee_assignments = {
        emp["name"]: {"days": 0, "assignments": {day: None for day in DAYS_OF_WEEK}}
        for emp in employees
    }

    for day in DAYS_OF_WEEK:
        random.shuffle(employees)
        for shift in SHIFTS:
            eligible = []

            for emp in employees:
                name = emp["name"]
                if (employee_assignments[name]["days"] < MAX_WORK_DAYS and
                    employee_assignments[name]["assignments"][day] is None and
                    emp["preferences"][day][shift] == 1):
                    eligible.append((name, 1))

            if len(eligible) < MIN_EMPLOYEES_PER_SHIFT:
                for emp in employees:
                    name = emp["name"]
                    if (employee_assignments[name]["days"] < MAX_WORK_DAYS and
                        employee_assignments[name]["assignments"][day] is None and
                        all(name != e[0] for e in eligible)):
                        pref_rank = emp["preferences"][day][shift]
                        eligible.append((name, pref_rank))
                        if len(eligible) >= MIN_EMPLOYEES_PER_SHIFT:
                            break

            for name, _ in eligible[:MIN_EMPLOYEES_PER_SHIFT]:
                schedule[day][shift].append(name)
                employee_assignments[name]["days"] += 1
                employee_assignments[name]["assignments"][day] = shift

    return schedule

# --- Print Schedule to Console ---
def print_schedule(schedule):
    print("\n🗓️  Weekly Employee Shift Schedule")
    print("=" * 40)
    for day in DAYS_OF_WEEK:
        print(f"\n{day}")
        for shift in SHIFTS:
            assigned = schedule[day][shift]
            shift_name = shift.capitalize()
            print(f"  {shift_name}: {', '.join(assigned) if assigned else 'No one assigned'}")

# --- Export Schedule to CSV ---
def export_schedule_to_csv(schedule, filename="weekly_schedule.csv"):
    with open(filename, mode='w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(["Day", "Shift", "Employees"])
        for day in DAYS_OF_WEEK:
            for shift in SHIFTS:
                employees = schedule[day][shift]
                writer.writerow([day, shift, ", ".join(employees)])
    print(f"\n✅ Schedule exported to {filename}")

# --- Run All ---
if __name__ == "__main__":
    employees = generate_employees()
    final_schedule = schedule_employees(employees)
    print_schedule(final_schedule)
    export_schedule_to_csv(final_schedule)