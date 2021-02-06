package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.entity.builder.JobGroupInstanceBuilder;
import com.navi.grabcodingexercise.model.JobGroupInstanceMessage;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class JobGroupInstanceExecutor {
    private Logger logger = LoggerFactory.getLogger(JobGroupInstanceExecutor.class);
    private final ExecutorService executorService;

    private final JobGroupInstanceRepository jobGroupInstanceRepository;

    public JobGroupInstanceExecutor(JobGroupInstanceRepository jobGroupInstanceRepository, ExecutorService executorService) {
        this.jobGroupInstanceRepository = jobGroupInstanceRepository;
        this.executorService = executorService;
    }

    public JobGroupInstanceMessage execute(JobGroupRequest request) {
        List<JobGroupInstance> instances = jobGroupInstanceRepository.findByGroupIdAndStatus(request.getGroupId(), ExecutionStatus.RUNNING);
        if(instances.size() > 0){
            String ids = instances.stream().map(JobGroupInstance::getGroupInstanceId).collect(Collectors.joining(","));
            throw new IllegalArgumentException(String.format("Following instances are already running %s", ids));
        }

        JobGroupInstance instance = new JobGroupInstanceBuilder(request).build();


    }
}
