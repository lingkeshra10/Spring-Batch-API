package com.lingkesh.springBatch.service;

import com.lingkesh.springBatch.entity.TransAccRecord;
import com.lingkesh.springBatch.model.SearchTransAccRecordModel;
import com.lingkesh.springBatch.model.UpdateTransAccRecordModel;

import java.util.List;
import java.util.Optional;

public interface TransAccRecordService {
    Optional<TransAccRecord> findTransAccRecordById(Long transAccRecordId);

    TransAccRecord retrieveTransAccRecordDetails(Long transAccRecordId);

    TransAccRecord updateTransAccRecord(Long transAccRecordId, UpdateTransAccRecordModel updateTransAccRecordModel);

    List<TransAccRecord> retrieveTransAccRecList();

    List<TransAccRecord> searchTransAccRecList(SearchTransAccRecordModel searchTransAccRecordModel);
}
