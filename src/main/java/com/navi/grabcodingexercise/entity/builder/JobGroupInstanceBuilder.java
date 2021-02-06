package com.navi.grabcodingexercise.entity.builder;

import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.model.JobGroupRequest;

import static com.navi.grabcodingexercise.util.DateTimeUtil.getCurrentTimeStamp;

public class JobGroupInstanceBuilder {

    final JobGroupInstance instance;
    JobGroupRequest request;

    public JobGroupInstanceBuilder(JobGroupRequest request) {
        instance = new JobGroupInstance();
        this.request = request;
    }

    public JobGroupInstance build() {
        instance.setGroupId(request.getGroupId());
        instance.setGroupInstanceId(request.getGroupId() + "-" + getCurrentTimeStamp());
        instance.setJobGroupSnapshot(request);
        instance.setStatus(ExecutionStatus.RUNNING);

        return instance;
    }
}
