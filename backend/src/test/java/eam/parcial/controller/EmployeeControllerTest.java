package eam.parcial.controller;

import eam.parcial.entity.Employee;
import eam.parcial.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService service;

    @InjectMocks
    private EmployeeController controller;

    private MockMvc mockMvc;
    private Employee employee;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        employee = new Employee(1L, "John", "Doe", 30, "john.doe@example.com", "123456789", "profile.jpg");
    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList() throws Exception {
        when(service.getAllEmployees()).thenReturn(Arrays.asList(employee));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].email", is(employee.getEmail())));
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenEmployeeExists() throws Exception {
        when(service.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void getEmployeeById_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        when(service.getEmployeeById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
        when(service.createEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"names\":\"John\",\"surnames\":\"Doe\",\"age\":30,\"email\":\"john.doe@example.com\",\"phoneNumber\":\"123456789\",\"picture\":\"profile.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee_WhenEmployeeExists() throws Exception {
        when(service.updateEmployee(eq(1L), any(Employee.class))).thenReturn(Optional.of(employee));

        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"names\":\"John Updated\",\"surnames\":\"Doe\",\"age\":31,\"email\":\"john.doe@example.com\",\"phoneNumber\":\"987654321\",\"picture\":\"updated.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names", is(employee.getNames())));
    }

    @Test
    void updateEmployee_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        when(service.updateEmployee(eq(1L), any(Employee.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployee_ShouldReturnNoContent_WhenEmployeeExists() throws Exception {
        when(service.deleteEmployee(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployee_ShouldReturnNotFound_WhenEmployeeDoesNotExist() throws Exception {
        when(service.deleteEmployee(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNotFound());
    }
}
