package streams;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartitionBy {
    public static void main(String[] args) {
        List<Employee> employees = EmployeeData.getEmployeeData();

        Map<Boolean, List<Employee>> x = employees.stream().collect(
                Collectors.partitioningBy(
                        (e) -> e.getSalary() >= 90000
                )
        );
        System.out.println(x);
    }
}
