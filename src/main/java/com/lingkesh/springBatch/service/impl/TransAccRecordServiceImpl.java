package com.lingkesh.springBatch.service.impl;

import com.lingkesh.springBatch.entity.TransAccRecord;
import com.lingkesh.springBatch.model.SearchTransAccRecordModel;
import com.lingkesh.springBatch.model.UpdateTransAccRecordModel;
import com.lingkesh.springBatch.repository.TransAccRecordRepo;
import com.lingkesh.springBatch.repository.specification.TransAccRecordList;
import com.lingkesh.springBatch.service.TransAccRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransAccRecordServiceImpl implements TransAccRecordService {
    TransAccRecordRepo transAccRecordRepo;

    @Autowired
    public TransAccRecordServiceImpl(TransAccRecordRepo transAccRecordRepo) {
        this.transAccRecordRepo = transAccRecordRepo;
    }

    @Override
    public Optional<TransAccRecord> findTransAccRecordById(Long transAccRecordId) {
        return transAccRecordRepo.findById(transAccRecordId);
    }

    @Override
    public TransAccRecord retrieveTransAccRecordDetails(Long transAccRecordId) {
        return transAccRecordRepo.findById(transAccRecordId)
                .orElseThrow(() -> new RuntimeException("Trans Acc Record not found with ID: " + transAccRecordId));
    }

    @Override
    public TransAccRecord updateTransAccRecord(Long transAccRecordId, UpdateTransAccRecordModel updateTransAccRecordModel) {
        // Check if the Trans Acc Record exists
        TransAccRecord existingTransAccRecord = transAccRecordRepo.findById(transAccRecordId)
                .orElseThrow(() -> new RuntimeException("Trans Acc Record not found with ID: " + transAccRecordId));

        // Compare old and new details
        boolean isUpdated = false;

        if(existingTransAccRecord.getCustomerId().equals(updateTransAccRecordModel.getCustomerId())){
            isUpdated = true;
            existingTransAccRecord.setCustomerId(updateTransAccRecordModel.getCustomerId());
        }

        if(existingTransAccRecord.getAccountNumber().equals(updateTransAccRecordModel.getAccountNumber())){
            isUpdated = true;
            existingTransAccRecord.setAccountNumber(updateTransAccRecordModel.getAccountNumber());
        }

        if(existingTransAccRecord.getDescription().equals(updateTransAccRecordModel.getDescription())){
            isUpdated = true;
            existingTransAccRecord.setDescription(updateTransAccRecordModel.getDescription());
        }

        if (isUpdated) {
            return transAccRecordRepo.save(existingTransAccRecord);
        } else {
            return existingTransAccRecord; // No changes, return existing object
        }
    }

    @Override
    public List<TransAccRecord> retrieveTransAccRecList() {
        return transAccRecordRepo.findAll();
    }

    @Override
    public List<TransAccRecord> searchTransAccRecList(SearchTransAccRecordModel searchTransAccRecord) {
        String start = searchTransAccRecord.getStart();
        String limit = searchTransAccRecord.getLimit();

        int startIndex = Integer.parseInt(start);
        int pageSize = Integer.parseInt(limit);
        Pageable pageable = PageRequest.of(startIndex, pageSize);

        Specification<TransAccRecord> spec = Specification.where(TransAccRecordList.hasId(searchTransAccRecord.getId()))
                .and(TransAccRecordList.hasCustId(searchTransAccRecord.getCustomerId()))
                .and(TransAccRecordList.hasAccountNumber(searchTransAccRecord.getAccountNumber()));

        return transAccRecordRepo.findAll(spec, pageable).getContent();
    }
}
