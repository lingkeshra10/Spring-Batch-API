package com.lingkesh.springBatch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTransAccRecordModel {
    private String customerId;
    private String accountNumber;
    private String description;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
