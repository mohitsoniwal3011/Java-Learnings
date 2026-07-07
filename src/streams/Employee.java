package streams;

public class Employee {

    private int id;
    private String name;
    private String department;
    private double salary;
    private int age;
    private String city;

    public Employee(int id, String name, String department,
                    double salary, int age, String city) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.age = age;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return name + " (" + department + ", " +"age "+age+ ", " + salary + ")";
    }
}

