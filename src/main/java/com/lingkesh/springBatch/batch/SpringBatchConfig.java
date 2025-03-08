package com.lingkesh.springBatch.batch;

import com.lingkesh.springBatch.entity.TransAccRecord;
import com.lingkesh.springBatch.repository.TransAccRecordRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringBatchConfig {
    @Autowired
    private TransAccRecordRepo transAccRecordRepo;

    @Bean
    public FlatFileItemReader<TransAccRecord> reader(){
        return new FlatFileItemReaderBuilder<TransAccRecord>()
                .name("accTransRecords")
                .resource(new ClassPathResource("data.txt"))
                .delimited()  // Use a delimiter-based tokenizer
                .delimiter(",")  // Set delimiter (adjust based on file format, e.g., CSV)
                .names("customerId", "accountNumber", "description") // Map column names
                .targetType(TransAccRecord.class)
                .build();
    }

    @Bean
    TransAccProcessor processor(){
        return new TransAccProcessor();
    }

    @Bean
    RepositoryItemWriter<TransAccRecord> writer(){
        RepositoryItemWriter<TransAccRecord> writer = new RepositoryItemWriter<>();
        writer.setRepository(transAccRecordRepo);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step processStep) {
        return new JobBuilder("importTransAcc", jobRepository)
                .start(processStep)
                .build();
    }

    @Bean
    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processStep", jobRepository)
                .<TransAccRecord, TransAccRecord>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
