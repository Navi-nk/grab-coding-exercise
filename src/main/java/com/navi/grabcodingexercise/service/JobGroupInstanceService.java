package com.navi.grabcodingexercise.service;

import com.navi.grabcodingexercise.entity.Group;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.jobexecutor.JobGroupInstanceExecutor;
import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import com.navi.grabcodingexercise.util.JsonConvertor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class JobGroupInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(JobGroupInstanceService.class);

    private final JobGroupInstanceRepository jobGroupInstanceRepository;
    private final JobGroupService jobGroupService;
    private final Environment environment;
    private ExecutorService executorService;

    public JobGroupInstanceService(JobGroupInstanceRepository jobGroupInstanceRepository, JobGroupService jobGroupService, Environment environment) {
        this.jobGroupInstanceRepository = jobGroupInstanceRepository;
        this.jobGroupService = jobGroupService;
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        int executorCount = environment.getProperty("job.executor.threads", Integer.class, 3);
        logger.info("Creating executor service with {} executor threads", executorCount);
        executorService = Executors.newFixedThreadPool(executorCount);
    }

    @PreDestroy
    public void preDestroy() throws InterruptedException {
        logger.info("Initiating executor service shutdown");
        executorService.shutdown();
        executorService.awaitTermination(1L, TimeUnit.MINUTES);
        logger.info("completed executor service shutdown");
    }

    public JobGroupInstanceMessage executeJobGroup(String groupId) {
        JobGroupRequest request = jobGroupService.getJobGroup(groupId);
        logger.info("Executing job group {}", JsonConvertor.toJsonString(request));
        try {
            return new JobGroupInstanceExecutor(jobGroupInstanceRepository, executorService).execute(request);
        }catch (Exception e) {
            if(e instanceof IllegalArgumentException || e instanceof NullPointerException || e.getCause() instanceof ConstraintViolationException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );
            throw e;
        }
    }

    public JobGroupInstanceMessage fetchJobGroupInstance(String jobGroupInstanceId) {
        return JobGroupInstanceMessage.from(
                mayBeFindGroupInstance(jobGroupInstanceId)
        );
    }

    private JobGroupInstance mayBeFindGroupInstance(String jobGroupInstanceId){
        Optional<JobGroupInstance> jobGroupInstance = jobGroupInstanceRepository.findByGroupInstanceId(jobGroupInstanceId);
        return jobGroupInstance.orElseThrow( () ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format("Job Group instance not found for id: %s", jobGroupInstanceId)
                )
        );
    }

}
