package com.PocFiltering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LTLRepository extends JpaRepository<LTLContract,Long> {
    Page<LTLContract> findAll(Specification<LTLContract> ltlContractSpecification, Pageable page);
}
