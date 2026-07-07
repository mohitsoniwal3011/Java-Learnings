package streams;

import java.util.*;
import java.util.stream.Collectors;

public class StreamGroupingByExample {
    public static void main(String[] args) {
        List<Employee> employees = EmployeeData.getEmployeeData();

        System.out.println("Group employees by department.");
//        Map<String , List<Employee>> map = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));
//        for(Map.Entry<String, List<Employee>> e : map.entrySet()){
//            System.out.println(e.getKey());
//            e.getValue().forEach(emp -> System.out.println(emp));
//        }

//        System.out.println("Group employees by department. for each department sort the employees with age");
//        Map<String , List<Employee>> map =
//                employees.stream().collect(Collectors.groupingBy(
//                        Employee::getDepartment,
//                        TreeMap::new,
//                        Collectors.collectingAndThen(
//                                Collectors.toList(),
//                                list -> {
//                                    list.sort(Comparator.comparing(Employee::getAge));
//                                    return list;
//                                }
//                        )
//                ));

//        for(Map.Entry<String, List<Employee>> e : map.entrySet()){
//            System.out.println(e.getKey());
//            e.getValue().forEach(emp -> System.out.println(emp));
//        }

//        System.out.println("Count employees in each department");
//        Map<String, Long> m=  employees.stream().collect(
//                Collectors.groupingBy(
//                        Employee::getDepartment,
//                        HashMap::new,
//                        Collectors.counting()
//                )
//        );
//        for(Map.Entry<String, Long> e : m.entrySet()){
//            System.out.println(e);
//        }

//        System.out.println("Count employees in each department");
//
//        Map<String, Double> map = employees.stream().collect(
//                Collectors.groupingBy(
//                        Employee::getDepartment,
//                        HashMap::new,
//                        Collectors.collectingAndThen(
//                                Collectors.averagingDouble(Employee::getSalary),
//                                avg -> (double)Math.round(avg*100)/ 100
//                        )
//                )
//        );
//        for(Map.Entry<String, Double> e : map.entrySet()){
//            System.out.println(e);
//        }

//        System.out.println("Highest paid employee in every department.");
//        Map<String , Double>  map = employees.stream().collect(
//                Collectors.groupingBy(
//                        Employee::getDepartment,
//                        HashMap::new,
//                        Collectors.collectingAndThen(
//                                Collectors.mapping(
//                                        Employee::getSalary,
//                                        Collectors.maxBy(Comparator.naturalOrder())
//                                ),
//                                Optional::get
//                        )
//                )
//        );
//
//        for(Map.Entry<String, Double> e : map.entrySet()){
//            System.out.println(e);
//        }

//        Map<String , DoubleSummaryStatistics> data = employees.stream().collect(
//                Collectors.groupingBy(
//                        Employee::getDepartment,
//                        HashMap::new,
//                        Collectors.summarizingDouble(
//                            Employee::getSalary
//                        )
//                )
//        );
//
//        for(Map.Entry<String, DoubleSummaryStatistics> e : data.entrySet()){
//            System.out.println(e);
//        }
    }
}
