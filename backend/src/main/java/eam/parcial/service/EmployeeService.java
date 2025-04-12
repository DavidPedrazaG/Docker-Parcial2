package eam.parcial.service;

import eam.parcial.entity.Employee;
import eam.parcial.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return repository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    public Optional<Employee> updateEmployee(Long id, Employee employeeDetails) {
        return repository.findById(id).map(employee -> {
            employee.setNames(employeeDetails.getNames());
            employee.setSurnames(employeeDetails.getSurnames());
            employee.setAge(employeeDetails.getAge());
            employee.setEmail(employeeDetails.getEmail());
            employee.setPhoneNumber(employeeDetails.getPhoneNumber());
            employee.setPicture(employeeDetails.getPicture());
            return repository.save(employee);
        });
    }

    public boolean deleteEmployee(Long id) {
        return repository.findById(id).map(employee -> {
            repository.delete(employee);
            return true;
        }).orElse(false);
    }
}
