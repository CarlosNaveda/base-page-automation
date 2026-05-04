package data;

import model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFactory {

    public static List<Employee> generateEmployeeByRecords (int numberRecords) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < numberRecords; i++) {
            employees.add(
                    new Employee(
                            "Name" + i,
                            "Last" + i,
                            20 + i,
                            "user" + i + "@mail.com",
                            3000 + i,
                            "QA"+ i
                    )
            );
        }
        return employees;
    }


}
