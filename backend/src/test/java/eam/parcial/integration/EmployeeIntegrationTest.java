package eam.parcial.integration;

import eam.parcial.entity.Employee;
import eam.parcial.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository repository;

    static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.6"))
            .withDatabaseName("testdb")
            .withUsername("testemployee")
            .withPassword("testpass");

    static {
        mariaDBContainer.start();
        System.setProperty("spring.datasource.url", mariaDBContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mariaDBContainer.getUsername());
        System.setProperty("spring.datasource.password", mariaDBContainer.getPassword());
    }

    private Employee employee;

    @BeforeEach
    void setUp() {
        repository.deleteAll(); // Limpiar la BD antes de cada prueba
        employee = new Employee(null, "Alice", "Johnson", 25, "alice@example.com", "987654321", "alice.jpg");
        employee = repository.save(employee);
    }

    @Test
    void whenGetEmployees_thenReturnsEmployeeList() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value(employee.getEmail()));
    }

    @Test
    void whenGetEmployeeById_thenReturnsEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/" + employee.getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(employee.getEmail()));
    }

    @Test
    void whenCreateEmployee_thenReturnsCreatedEmployee() throws Exception {
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"names\":\"Bob\",\"surnames\":\"Brown\",\"age\":30,\"email\":\"bob@example.com\",\"phoneNumber\":\"123456789\",\"picture\":\"bob.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("bob@example.com"));

        Optional<Employee> newEmployee = repository.findByEmail("bob@example.com");
        assertThat(newEmployee).isPresent();
    }

    @Test
    void whenUpdateEmployee_thenReturnsUpdatedEmployee() throws Exception {
        mockMvc.perform(put("/api/employees/" + employee.getEmployeeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"names\":\"Alice Updated\",\"surnames\":\"Johnson\",\"age\":26,\"email\":\"alice@example.com\",\"phoneNumber\":\"123456789\",\"picture\":\"newpic.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").value("Alice Updated"));

        Employee updatedEmployee = repository.findById(employee.getEmployeeId()).orElseThrow();
        assertThat(updatedEmployee.getNames()).isEqualTo("Alice Updated");
    }

    @Test
    void whenDeleteEmployee_thenEmployeeIsRemoved() throws Exception {
        mockMvc.perform(delete("/api/employees/" + employee.getEmployeeId()))
                .andExpect(status().isNoContent());

        Optional<Employee> deletedEmployee = repository.findById(employee.getEmployeeId());
        assertThat(deletedEmployee).isEmpty();
    }
}
