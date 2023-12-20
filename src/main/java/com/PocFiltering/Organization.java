package com.PocFiltering;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organizations")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Organization {


    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    private OrganizationType type;
    @ManyToOne
    @JoinColumn(name = "organization_type_id", nullable = false)
    public OrganizationType getType() {
        return type;
    }
}

