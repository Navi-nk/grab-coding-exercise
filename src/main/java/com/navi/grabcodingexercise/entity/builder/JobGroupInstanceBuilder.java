package com.navi.grabcodingexercise.entity.builder;

import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.model.JobGroupRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JobGroupInstanceBuilder {

    final JobGroupInstance instance;
    JobGroupRequest request;

    public JobGroupInstanceBuilder(JobGroupRequest request) {
        instance = new JobGroupInstance();
        this.request = request;
    }

    public JobGroupInstance build() {
        instance.setGroupId(request.getGroupId());
        instance.setGroupInstanceId(createInstanceId());
        instance.setJobGroupSnapshot(request);
        instance.setStatus(ExecutionStatus.RUNNING);

        return instance;
    }

    private String createInstanceId() {
        String dt = DateTimeFormatter.ofPattern("yyyyMMdd-hhmiss").format(LocalDateTime.now());
        return request.getGroupId() + "-" + dt;
    }
}
