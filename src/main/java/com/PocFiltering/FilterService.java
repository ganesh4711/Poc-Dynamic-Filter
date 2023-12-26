package com.PocFiltering;

import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
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


    public static Specification<LTLContract> applyFilters(long orgId, Map<String, String> filters) throws RuntimeException {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("organization").get("id"), orgId));

            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String field = entry.getKey();
                String[] valuesArray = entry.getValue().toLowerCase().split(",");
                List<String> values = List.of(valuesArray);

                if (!values.isEmpty()) {
                    try {
                        switch (field) {
                            case "contractedOrgName":
                                addLikePredicate(builder, predicates, root.join("contractedOrganization", JoinType.INNER).get("name"), values);
                                break;
                            case "orgType":
                                Join<LTLContract, Organization> join = root.join("contractedOrganization", JoinType.INNER);
                                addLikePredicate(builder, predicates, join.join("type", JoinType.INNER).get("name"), values);
                                break;
                            case "effectiveDate":
                                List<LocalDate> dates = values.stream().map(LocalDate::parse).toList();
                                predicates.add(builder.greaterThanOrEqualTo(root.get("effectiveDate"), Collections.min(dates)));
                                break;
                            case "expiryDate":
                                List<LocalDate> expiryDates = values.stream().map(LocalDate::parse).toList();
                                predicates.add(builder.lessThanOrEqualTo(root.get("expiryDate"), Collections.max(expiryDates)));
                                break;
                            default:
                                throw new NoSuchFieldException("Filter field not found");
                        }
                    } catch (DateTimeParseException | NoSuchFieldException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addLikePredicate(CriteriaBuilder builder, List<Predicate> predicates, Expression<String> expression, List<String> values) {
        List<Predicate> likePredicates = values.stream()
                .map(value -> builder.like(builder.lower(expression), "%" + value + "%"))
                .toList();
        predicates.add(builder.or(likePredicates.toArray(new Predicate[0])));
    }

}