package streams;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamsLevelUp {
    public static void main(String[] args) {
        List<Employee> employees = EmployeeData.getEmployeeData();
        // ========= Level 4 =======

        // print all engineering employees
        // pred is not much useful but this is just for learning prupose

//        Predicate<Employee> pred = new Predicate<Employee>() {
//            @Override
//            public boolean test(Employee employee) {
//                return "Engineering".equals(employee.getDepartment());
//            }
//        };
//        employees.stream().filter(pred).forEach(System.out::println);

//        System.out.println("Print names of all employees");
        //Print names of all employees.
//        employees.stream().forEach(e -> System.out.println(e.getName()));
//        employees.stream().map(Employee::getName).forEach(System.out::println);

//        System.out.println("Employees earning more than ₹90,000.");
        //employees.stream().filter(e -> e.getSalary() > 90000).forEach(System.out::println);

//        System.out.println("Employees older than 30.");
//        employees.stream().filter(e -> e.getAge() > 30).forEach(System.out::println);

//        Comparator<Employee> comp = new Comparator<Employee>() {
//            @Override
//            public int compare(Employee e1, Employee e2) {
//                return (int) (e1.getSalary() - e2.getSalary());
//            }
//        };
//        List<Employee> sortedSalary = employees.stream().sorted(comp).toList();
//        sortedSalary.forEach(System.out::println);



//        System.out.println("Sort by age descending");
//        List<Employee> sortedSalary = employees.stream().sorted((e1, e2) -> e2.getAge() - e1.getAge()).toList();
//        sortedSalary.forEach(System.out::println);


//        System.out.println("Employee with highest salary");
//        System.out.println(employees.stream().min((e1, e2) -> (int) (e2.getSalary() - e1.getSalary())).get());
//        System.out.println(employees.stream().sorted((e1, e2) -> (int) (e2.getSalary() - e1.getSalary())).findFirst().get());

//        System.out.println("Employee with lowest salary");
//        System.out.println(employees.stream().min((e1, e2) -> (int) (e1.getSalary() - e2.getSalary())).get());
//        System.out.println(employees.stream().sorted((e1, e2) -> (int) (e1.getSalary() - e2.getSalary())).findFirst().get());

//        System.out.println("Count employees in Engineering.");
//        System.out.println(employees.stream().filter(e -> "Engineering".equals(e.getDepartment())).count());

//        System.out.println("Average salary.");
//        System.out.println(employees.stream().mapToDouble(Employee::getSalary).average().getAsDouble());




    }


}
