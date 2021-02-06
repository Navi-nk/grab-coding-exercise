package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.entity.builder.JobGroupInstanceBuilder;
import com.navi.grabcodingexercise.jobexecutor.async.JobGroupExecutor;
import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import com.navi.grabcodingexercise.repository.JobInstanceRepository;
import com.navi.grabcodingexercise.util.JsonConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class JobGroupInstanceExecutor {
    private Logger logger = LoggerFactory.getLogger(JobGroupInstanceExecutor.class);
    private final ExecutorService executorService;
    private final ConcurrentHashMap<String, Future<String>> jobTrackerMap;

    private final JobGroupInstanceRepository jobGroupInstanceRepository;
    private final JobInstanceRepository jobInstanceRepository;

    public JobGroupInstanceExecutor(ExecutorService executorService, ConcurrentHashMap<String, Future<String>> jobTrackerMap, JobGroupInstanceRepository jobGroupInstanceRepository, JobInstanceRepository jobInstanceRepository) {
        this.executorService = executorService;
        this.jobTrackerMap = jobTrackerMap;
        this.jobGroupInstanceRepository = jobGroupInstanceRepository;
        this.jobInstanceRepository = jobInstanceRepository;
    }

    public JobGroupInstanceMessage execute(JobGroupRequest request) {
        List<JobGroupInstance> instances = jobGroupInstanceRepository.findByGroupIdAndStatus(request.getGroupId(), ExecutionStatus.RUNNING);
        if (instances.size() > 0) {
            String ids = instances.stream().map(JobGroupInstance::getGroupInstanceId).collect(Collectors.joining(","));
            throw new IllegalArgumentException(String.format("Following instances are already running %s", ids));
        }

        JobGroupInstance instance = new JobGroupInstanceBuilder(request).build();
        JobGroupInstance savedInstance = jobGroupInstanceRepository.save(instance);
        logger.info("Job instance {}", JsonConvertor.toJsonString(savedInstance));

        Future<String> jobGroupFuture = executorService.submit(
                new JobGroupExecutor(savedInstance, request, jobGroupInstanceRepository, jobInstanceRepository));

        jobTrackerMap.putIfAbsent(instance.getGroupInstanceId(), jobGroupFuture);

        return JobGroupInstanceMessage.from(savedInstance);

    }
}
