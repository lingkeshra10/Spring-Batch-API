package com.lingkesh.springBatch.repository.specification;

import com.lingkesh.springBatch.entity.TransAccRecord;
import org.springframework.data.jpa.domain.Specification;

public class TransAccRecordList {

    public static Specification<TransAccRecord> hasId(String id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<TransAccRecord> hasCustId(String customerId) {
        return (root, query, criteriaBuilder) -> {
            if (customerId == null || customerId.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("customerId"), customerId);
        };
    }

    public static Specification<TransAccRecord> hasAccountNumber(String accountNumber) {
        return (root, query, criteriaBuilder) -> {
            if (accountNumber == null || accountNumber.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
        };
    }
}
