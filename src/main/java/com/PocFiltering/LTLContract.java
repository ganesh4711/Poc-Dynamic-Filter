package com.PocFiltering;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ltl_contracts")
public class LTLContract{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    @ManyToOne
    @JoinColumn(name = "contracted_organization_id", nullable = false)
    private Organization contractedOrganization;

    @Column(name = "rpm_consideration")
    private Boolean rpmConsideration;

    @Column(name = "rpm_value")
    private Double rpmValue;


    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;


    public Organization getOrganization() {
        return organization;
    }

    public Organization getContractedOrganization() {
        return contractedOrganization;

    }

      public LTLContract(Organization organization, Organization contractedOrganization, Boolean rpmConsideration,
                         Double rpmValue, LocalDate effectiveDate, LocalDate expiryDate) {
        this.organization = organization;
        this.contractedOrganization = contractedOrganization;
        this.rpmConsideration = rpmConsideration;
        this.rpmValue = rpmValue;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
    
    }

    public void updateLtlContract(Organization organization, Organization contractedOrganization,
                                  Boolean considerRPM, Double rpmValue,
                                  LocalDate effectiveDate, LocalDate expiryDate) {

        this.organization = organization;
        this.contractedOrganization = contractedOrganization;
        this.rpmConsideration = considerRPM;
        this.rpmValue = rpmValue;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
    }
}