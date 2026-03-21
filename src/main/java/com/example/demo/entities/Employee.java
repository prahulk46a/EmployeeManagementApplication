package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;


@Entity
@Table()
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private Long employeeId;
	
	private String firstName;
    private String lastName;
    private String email;
    private String department;
}
