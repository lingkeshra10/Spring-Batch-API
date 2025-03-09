package com.lingkesh.springBatch.batch;

import com.lingkesh.springBatch.entity.TransAccRecord;
import com.lingkesh.springBatch.repository.TransAccRecordRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

@Configuration
public class SpringBatchConfig {
    @Autowired
    private TransAccRecordRepo transAccRecordRepo;

    @Bean
    @StepScope
    public FlatFileItemReader<TransAccRecord> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        Assert.notNull(filePath, "Path must not be null");
        return new FlatFileItemReaderBuilder<TransAccRecord>()
                .name("accTransRecordsReader")
                .resource(new FileSystemResource(filePath)) // Dynamically load the uploaded file
                .delimited()
                .delimiter(",")  // Adjust for CSV/TXT format
                .names("customerId", "accountNumber", "description") // Column mappings
                .targetType(TransAccRecord.class)
                .build();
    }

    @Bean
    TransAccProcessor processor() {
        return new TransAccProcessor();
    }

    @Bean
    RepositoryItemWriter<TransAccRecord> writer() {
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
    public Step processStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            FlatFileItemReader<TransAccRecord> reader) {
        return new StepBuilder("processStep", jobRepository)
                .<TransAccRecord, TransAccRecord>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor())
                .writer(writer())
                .build();
    }
}
