package com.lingkesh.springBatch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel {
    int code;
    String message;
    String object;

    public final static int AUTH_LOGIN_SUCCESS = 10001;
    public final static int IMPORT_TRANS_ACC_RECORD_FAILED = 10002;
    public final static int IMPORT_TRANS_ACC_RECORD_SUCCESS = 10003;
    public final static int UPDATE_TRANS_ACC_RECORD_SUCCESS = 10004;
    public final static int RETRIEVE_TRANS_ACC_RECORD_DETAILS_SUCCESS = 10005;

    public final static int RETRIEVE_TRANS_ACC_RECORDS_SUCCESS = 10006;
    public final static int INVALID_CUSTOMER_ID = 10007;
    public final static int INVALID_ACCOUNT_NO = 10008;
    public final static int TRANS_ACC_RECORD_DETAILS_UNAVAILABLE = 10009;
    public final static int TRANS_ACC_RECORDS_FOUND_SUCCESS = 10010;

    public final static int TRANS_ACC_RECORDS_NOT_FOUND = 10011;
    public final static int TRANS_ACC_RECORDS_FOUND = 10012;

    public final static int EXCEPTION_ERROR = 500;

    public static String getResponseMsg(int code){
        return switch (code) {
            case AUTH_LOGIN_SUCCESS -> "Login Success";
            case IMPORT_TRANS_ACC_RECORD_FAILED -> "File is empty. Please upload a valid CSV or TXT file.";
            case IMPORT_TRANS_ACC_RECORD_SUCCESS -> "Import Trans Acc Record Success";
            case UPDATE_TRANS_ACC_RECORD_SUCCESS -> "Update trans account record success";
            case RETRIEVE_TRANS_ACC_RECORD_DETAILS_SUCCESS -> "Retrieve trans account record details success";

            case RETRIEVE_TRANS_ACC_RECORDS_SUCCESS -> "Retrieve trans account records success";
            case INVALID_CUSTOMER_ID -> "Invalid Customer ID";
            case INVALID_ACCOUNT_NO -> "Invalid Account Number";
            case TRANS_ACC_RECORD_DETAILS_UNAVAILABLE -> "Transaction account record details unavailable";
            case TRANS_ACC_RECORDS_FOUND_SUCCESS -> "Transaction account records found successfully";

            case TRANS_ACC_RECORDS_NOT_FOUND -> "No transaction account records found.";
            case TRANS_ACC_RECORDS_FOUND -> "Transaction account records found.";

            case EXCEPTION_ERROR -> "Exception Error: ";
            default -> "";
        };
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
