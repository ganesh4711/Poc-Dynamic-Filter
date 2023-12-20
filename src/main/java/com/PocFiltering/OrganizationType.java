package com.PocFiltering;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "organization_types")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrganizationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;
    @Column(name = "name")
    private String name;

    @Column(name = "deleted_at")
    private Date deletedAt;

}