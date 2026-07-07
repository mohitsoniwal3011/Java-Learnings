package streams;

import java.util.ArrayList;
import java.util.List;

public class EmployeeData {
    private static final List<Employee> EMPLOYEE_DATA =List.of(

            new Employee(1, "Mohit", "Engineering", 95000, 26, "Bangalore"),
            new Employee(2, "Rahul", "Engineering", 87000, 28, "Bangalore"),
            new Employee(3, "Ankit", "Engineering", 120000, 31, "Pune"),

            new Employee(4, "Sneha", "HR", 60000, 29, "Delhi"),
            new Employee(5, "Priya", "HR", 65000, 32, "Bangalore"),

            new Employee(6, "Aman", "Sales", 75000, 30, "Mumbai"),
            new Employee(7, "Neha", "Sales", 82000, 27, "Delhi"),
            new Employee(8, "Rohit", "Sales", 78000, 25, "Mumbai"),

            new Employee(9, "Karan", "Finance", 91000, 34, "Pune"),
            new Employee(10, "Pooja", "Finance", 89000, 30, "Delhi"),

            new Employee(11, "Arjun", "Engineering", 120000, 29, "Hyderabad"),
            new Employee(12, "Simran", "Engineering", 95000, 27, "Hyderabad"),

            new Employee(13, "Deepak", "Marketing", 70000, 33, "Mumbai"),
            new Employee(14, "Nisha", "Marketing", 72000, 28, "Delhi"),

            new Employee(15, "Varun", "Engineering", 87000, 35, "Pune")
    );

    public static List<Employee> getEmployeeData(){
        return new ArrayList<>(EMPLOYEE_DATA);
    }
}
