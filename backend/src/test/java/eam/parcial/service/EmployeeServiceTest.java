package eam.parcial.service;

import eam.parcial.entity.Employee;
import eam.parcial.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John", "Doe", 30, "john.doe@example.com", "123456789", "profile.jpg");
    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList() {
        when(repository.findAll()).thenReturn(Arrays.asList(employee));
        List<Employee> employees = service.getAllEmployees();
        assertEquals(1, employees.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenEmployeeExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        Optional<Employee> foundEmployee = service.getEmployeeById(1L);
        assertTrue(foundEmployee.isPresent());
        assertEquals(employee.getEmail(), foundEmployee.get().getEmail());
    }

    @Test
    void getEmployeeById_ShouldReturnEmpty_WhenEmployeeDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Employee> foundEmployee = service.getEmployeeById(1L);
        assertFalse(foundEmployee.isPresent());
    }

    @Test
    void createEmployee_ShouldReturnSavedEmployee() {
        when(repository.save(employee)).thenReturn(employee);
        Employee savedEmployee = service.createEmployee(employee);
        assertNotNull(savedEmployee);
        assertEquals(employee.getEmail(), savedEmployee.getEmail());
        verify(repository, times(1)).save(employee);
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee_WhenEmployeeExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        when(repository.save(any(Employee.class))).thenReturn(employee);
        
        Employee updatedDetails = new Employee(null, "Jane", "Doe", 28, "jane.doe@example.com", "987654321", "newpic.jpg");
        Optional<Employee> updatedEmployee = service.updateEmployee(1L, updatedDetails);
        
        assertTrue(updatedEmployee.isPresent());
        assertEquals("Jane", updatedEmployee.get().getNames());
    }

    @Test
    void updateEmployee_ShouldReturnEmpty_WhenEmployeeDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Employee> updatedEmployee = service.updateEmployee(1L, employee);
        assertFalse(updatedEmployee.isPresent());
    }

    @Test
    void deleteEmployee_ShouldReturnTrue_WhenEmployeeExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        boolean deleted = service.deleteEmployee(1L);
        assertTrue(deleted);
        verify(repository, times(1)).delete(employee);
    }

    @Test
    void deleteEmployee_ShouldReturnFalse_WhenEmployeeDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        boolean deleted = service.deleteEmployee(1L);
        assertFalse(deleted);
    }
}
