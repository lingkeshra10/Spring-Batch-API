package com.lingkesh.springBatch.controller;

import com.lingkesh.springBatch.entity.TransAccRecord;
import com.lingkesh.springBatch.model.ResponseModel;
import com.lingkesh.springBatch.model.SearchTransAccRecordModel;
import com.lingkesh.springBatch.model.UpdateTransAccRecordModel;
import com.lingkesh.springBatch.service.TransAccRecordService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transAcc")
public class TransAccController {
    @Autowired
    private Job job;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private TransAccRecordService transAccRecordService;

    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> importData(@RequestParam("file") MultipartFile file) {
        ResponseModel responseModel = new ResponseModel();

        if (file.isEmpty()) {
            responseModel.setCode(ResponseModel.IMPORT_TRANS_ACC_RECORD_FAILED);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.IMPORT_TRANS_ACC_RECORD_FAILED));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }

        try {
            // Save the uploaded file to a temporary location
            Path tempFilePath = Files.createTempFile("transAcc_", "_" + file.getOriginalFilename());
            Files.write(tempFilePath, file.getBytes());

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", tempFilePath.toString()) // Pass the file path dynamically
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            responseModel.setCode(ResponseModel.IMPORT_TRANS_ACC_RECORD_SUCCESS);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.IMPORT_TRANS_ACC_RECORD_SUCCESS));
            responseModel.setObject(jobExecution.getStatus().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
        } catch (Exception ex) {
            responseModel.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModel.setMessage("Error processing file: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }

    //Update Trans Acc Records
    @RequestMapping(value = "/updateData/{transAccRecId}", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ResponseModel> updateData(@PathVariable Long transAccRecId, @RequestBody UpdateTransAccRecordModel updateTransAccRecordModel) {
        ResponseModel responseModel = new ResponseModel();
        try{
            Optional<TransAccRecord> checkTransExist = transAccRecordService.findTransAccRecordById(transAccRecId);
            if (checkTransExist.isPresent()){
                TransAccRecord transAccRecord = transAccRecordService.updateTransAccRecord(transAccRecId, updateTransAccRecordModel);

                responseModel.setCode(ResponseModel.UPDATE_TRANS_ACC_RECORD_SUCCESS);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.UPDATE_TRANS_ACC_RECORD_SUCCESS));
                responseModel.setObject(transAccRecord.toString());
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }else {
                responseModel.setCode(ResponseModel.TRANS_ACC_RECORD_DETAILS_UNAVAILABLE);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.TRANS_ACC_RECORD_DETAILS_UNAVAILABLE));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
            }
        }catch (Exception ex){
            responseModel.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }

    //Retrieve Trans Acc Records Details
    @RequestMapping(value = "/retrieveDataDetails/{transAccRecId}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> retrieveDataDetails(@PathVariable Long transAccRecId) {
        ResponseModel responseModel = new ResponseModel();
        try{
            Optional<TransAccRecord> checkTransExist = transAccRecordService.findTransAccRecordById(transAccRecId);
            if (checkTransExist.isPresent()){

                TransAccRecord transAccRecord = transAccRecordService.retrieveTransAccRecordDetails(transAccRecId);

                responseModel.setCode(ResponseModel.RETRIEVE_TRANS_ACC_RECORDS_SUCCESS);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.RETRIEVE_TRANS_ACC_RECORDS_SUCCESS));
                responseModel.setObject(transAccRecord.toString());
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }else {
                responseModel.setCode(ResponseModel.TRANS_ACC_RECORD_DETAILS_UNAVAILABLE);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.TRANS_ACC_RECORD_DETAILS_UNAVAILABLE));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
            }
        }catch (Exception ex){
            responseModel.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }

    //Retrieve Trans Acc Records
    @RequestMapping(value = "/retrieveDataList", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<ResponseModel> retrieveTransAccRecList() {
        ResponseModel responseModel = new ResponseModel();
        try {
            List<TransAccRecord> transAccRecordList = transAccRecordService.retrieveTransAccRecList();

            if(transAccRecordList.isEmpty()){
                responseModel.setCode(ResponseModel.TRANS_ACC_RECORDS_NOT_FOUND);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.TRANS_ACC_RECORDS_NOT_FOUND));
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }

            responseModel.setCode(ResponseModel.TRANS_ACC_RECORDS_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.TRANS_ACC_RECORDS_FOUND));
            responseModel.setObject(transAccRecordList.toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);

        }catch (Exception ex){
            responseModel.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }

    //Search Trans Acc Records
    @RequestMapping(value = "/searchDataList", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<ResponseModel> searchTransAccRecList(@RequestBody SearchTransAccRecordModel searchTransAccRecordModel) {
        ResponseModel responseModel = new ResponseModel();
        try {
            List<TransAccRecord> transAccRecordList = transAccRecordService.searchTransAccRecList(searchTransAccRecordModel);

            if(transAccRecordList.isEmpty()){
                responseModel.setCode(ResponseModel.TRANS_ACC_RECORDS_NOT_FOUND);
                responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.TRANS_ACC_RECORDS_NOT_FOUND));
                return ResponseEntity.status(HttpStatus.OK).body(responseModel);
            }

            responseModel.setCode(ResponseModel.TRANS_ACC_RECORDS_FOUND);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.TRANS_ACC_RECORDS_FOUND));
            responseModel.setObject(transAccRecordList.toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseModel);

        }catch (Exception ex){
            responseModel.setCode(ResponseModel.EXCEPTION_ERROR);
            responseModel.setMessage(ResponseModel.getResponseMsg(ResponseModel.EXCEPTION_ERROR)  + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
    }
}