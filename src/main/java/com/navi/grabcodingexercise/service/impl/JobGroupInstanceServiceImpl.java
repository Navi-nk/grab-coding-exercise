package com.navi.grabcodingexercise.service.impl;

import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.jobexecutor.JobGroupInstanceExecutor;
import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import com.navi.grabcodingexercise.repository.JobInstanceRepository;
import com.navi.grabcodingexercise.service.JobGroupInstanceService;
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
import java.util.concurrent.*;

@Service
public class JobGroupInstanceServiceImpl implements JobGroupInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(JobGroupInstanceServiceImpl.class);

    private final JobGroupInstanceRepository jobGroupInstanceRepository;
    private final JobInstanceRepository jobInstanceRepository;
    private final JobGroupServiceImpl jobGroupService;
    private final Environment environment;
    private ExecutorService executorService;
    private ConcurrentHashMap<String, Future<String>> jobTrackerMap;

    public JobGroupInstanceServiceImpl(JobGroupInstanceRepository jobGroupInstanceRepository, JobInstanceRepository jobInstanceRepository, JobGroupServiceImpl jobGroupService, Environment environment) {
        this.jobGroupInstanceRepository = jobGroupInstanceRepository;
        this.jobInstanceRepository = jobInstanceRepository;
        this.jobGroupService = jobGroupService;
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        int executorCount = environment.getProperty("job.executor.threads", Integer.class, 3);
        logger.info("Creating executor service with {} executor threads", executorCount);
        executorService = Executors.newFixedThreadPool(executorCount);
        jobTrackerMap = new ConcurrentHashMap<>();
    }

    @PreDestroy
    public void preDestroy() throws InterruptedException {
        logger.info("Initiating executor service shutdown");
        executorService.shutdownNow();
        executorService.awaitTermination(1L, TimeUnit.MINUTES);
        logger.info("completed executor service shutdown");
    }

    @Override
    public JobGroupInstanceMessage executeJobGroup(String groupId) {
        JobGroupRequest request = jobGroupService.getJobGroup(groupId);
        logger.info("Executing job group {}", JsonConvertor.toJsonString(request));
        try {
            return new JobGroupInstanceExecutor(executorService, jobTrackerMap,jobGroupInstanceRepository, jobInstanceRepository).execute(request);
        }catch (Exception e) {
            if(e instanceof IllegalArgumentException || e instanceof NullPointerException || e.getCause() instanceof ConstraintViolationException)
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage()
                );
            throw e;
        }
    }

    @Override
    public JobGroupInstanceMessage fetchJobGroupInstance(String jobGroupInstanceId) {
        logger.info("Getting job group instance for {}", jobGroupInstanceId);
        return JobGroupInstanceMessage.from(
                mayBeFindGroupInstance(jobGroupInstanceId)
        );
    }

    @Override
    public void cancelJobGroupExecution(String jobGroupInstanceId) {
        if(!jobTrackerMap.containsKey(jobGroupInstanceId)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Job Instance for id %s not found", jobGroupInstanceId)
            );
        }
        logger.info("Cancelling job group instance: {}", jobGroupInstanceId);
        Future<String> f = jobTrackerMap.get(jobGroupInstanceId);
        f.cancel(true);
        jobTrackerMap.remove(jobGroupInstanceId);
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
