package com.lingkesh.springBatch.repository;

import com.lingkesh.springBatch.entity.TransAccRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransAccRecordRepo extends JpaRepository<TransAccRecord, Long> {
    Page<TransAccRecord> findAll(Specification<TransAccRecord> spec, Pageable pageable);
}
