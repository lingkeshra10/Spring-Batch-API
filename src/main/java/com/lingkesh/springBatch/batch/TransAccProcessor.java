package com.lingkesh.springBatch.batch;

import com.lingkesh.springBatch.entity.TransAccRecord;
import org.springframework.batch.item.ItemProcessor;

public class TransAccProcessor implements ItemProcessor<TransAccRecord, TransAccRecord> {

    @Override
    public TransAccRecord process(TransAccRecord transAccRecord) throws Exception {
        try {
            transAccRecord.setCustomerId(transAccRecord.getCustomerId());
            transAccRecord.setAccountNumber(transAccRecord.getAccountNumber());
            transAccRecord.setDescription(transAccRecord.getDescription());
            return transAccRecord;
        } catch (Exception e) {
            System.err.println("Error processing record: " + transAccRecord);
            throw e;
        }
    }
}
