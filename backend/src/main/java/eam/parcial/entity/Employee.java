package eam.parcial.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    
    @Column(nullable = false)
    private String names;
    
    @Column(nullable = false)
    private String surnames;
    
    private Integer age;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phoneNumber;
    
    private String picture;
}
