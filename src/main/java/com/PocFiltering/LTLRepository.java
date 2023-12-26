package com.PocFiltering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LTLRepository extends JpaRepository<LTLContract,Long> {
    Page<LTLContract> findAll(Specification<LTLContract> ltlContractSpecification, Pageable page);

    @Query(value = "select lc.* from ltl_contracts lc " +
            "join organizations o on o.id=lc.organization_id " +
            "join organizations o2 on o2.id =lc.contracted_organization_id " +
            "join organization_types ot on ot.id =o2.organization_type_id " +
            "where organization_id = ?1 " +
            "AND (?2 IS NULL OR LOWER(o2.name) ILIKE LOWER(?2) || '%') " +
            "and (?3 is null or lower(ot.name) ilike lower(?3) || '%') " +
            "and (cast(?4 as DATE) is null or lc.effective_date >= ?4) " +
            "and (cast(?5 as DATE) is null or lc.expiry_date  >= ?5)",nativeQuery = true)
    Page<LTLContract> findfilteredContracts(long orgId, String orgName, String orgType, LocalDate effective, LocalDate expiry, PageRequest page);
}
