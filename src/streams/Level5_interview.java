package streams;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Level5_interview {
    /**
     * Questions are below
     *
     *
     * Second highest salary.
     * Top 3 highest paid employees.
     * Employees sorted by
     * salary descending
     * if salary same → name ascending
     * Department having maximum employees.
     * City having highest average salary.
     * Youngest employee in every department.
     * Oldest employee in every department.
     * Total salary paid department-wise.
     * Employees whose names start with 'A'.
     * Names of employees sorted alphabetically.
     *
     */
    public static void main(String[] args) {
        List<Employee> employess = EmployeeData.getEmployeeData();
        //q1
//        System.out.println(
//                employess
//                        .stream()
//                        .map(Employee::getSalary)
//                        .distinct()
//                        .sorted(Comparator.reverseOrder())
//                        .skip(1).findFirst().get()
//        );

        //q2  Top 3 highest paid employees.
//        employess
//                .stream()
//                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
//                .limit(3)
//                .forEach(System.out::println);

        //q3 employees sorted by salary descending
//        employess
//                .stream()
//                .sorted((e1, e2) -> {
//                    if(e1.getSalary() == e2.getSalary()){
//                        return e1.getName().compareTo(e2.getName());
//                    }
//                    return Double.compare(e2.getSalary(), e1.getSalary());
//                })
//                .forEach(System.out::println);

        //q4 Department having maximum employees
//        Map<String , Long > map = employess.stream().collect(
//                Collectors.groupingBy(
//                        Employee::getDepartment,
//                        HashMap::new,
//                        Collectors.counting()
//                )
//        );
//
//        System.out.println(
//                map.values().stream().max(Comparator.naturalOrder()).get()
//        );

        // city with highest avg salaries
        Map<String , Double> map = employess.stream().collect(
                Collectors.groupingBy(
                        Employee::getCity,
                        HashMap::new,
                        Collectors.averagingDouble(
                                Employee::getSalary
                        )
                )
        );
        System.out.println(map
                .entrySet()
                .stream()
                .sorted((e1,e2 ) -> Double.compare(e2.getValue(), e1.getValue()))
                .findFirst()
                .get());





    }
}
