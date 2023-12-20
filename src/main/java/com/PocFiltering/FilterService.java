package com.PocFiltering;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FilterService {
    @Autowired
    private LTLRepository repository;

    public List<LTLContract> getAllLtlContractsByOrgIdAndFilter(long orgId, Map<String, String> filter, int pageNumber, int pageSize) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<LTLContract> ltlContractPage = repository.findAll(applyFilters(orgId, filter), page);

        return ltlContractPage.getContent();
    }


    public List<LTLContract> getAllFilteredContracts(long orgId, String orgName, String orgType, LocalDate effective, LocalDate expiry, int pageNumber, int pageSize)
    {
        PageRequest page = PageRequest.of(pageNumber, pageSize);
       Page<LTLContract> ltlContractPage= repository.findfilteredContracts(orgId,orgName,orgType,effective,expiry,page);
       return ltlContractPage.getContent();
    }

    static Specification<LTLContract> applyFilters(long orgId, Map<String, String> filters) throws RuntimeException{
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("organization").get("id"), orgId));
            filters.forEach((field, value) -> {
                if (value != null) {
                    switch (field) {
                        case "contractedOrgName":
                            Join<LTLContract, Organization> organizationJoin = root.join("contractedOrganization", JoinType.INNER);
                            predicates.add(builder.like(builder.lower(organizationJoin.get("name")), "%" + (value).toLowerCase() + "%"));
                            break;
                        case "orgType":
                            Join<LTLContract, Organization> join = root.join("contractedOrganization", JoinType.INNER);
                            Join<Organization, OrganizationType> organizationTypeJoin = join.join("type", JoinType.INNER);
                            predicates.add(builder.like(builder.lower(organizationTypeJoin.get("name")), "%" + (value).toLowerCase() + "%"));
                            break;
                        case "effectiveDate":
                            predicates.add(builder.greaterThanOrEqualTo(root.get("effectiveDate"), LocalDate.parse(value)));
                            break;
                        case "expiryDate":
                            predicates.add(builder.lessThanOrEqualTo(root.get("expiryDate"), LocalDate.parse(value)));
                            break;
                        default:
                            throw new RuntimeException("Filter not found");
                    }
                }
            });
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
