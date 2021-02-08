package com.navi.grabcodingexercise.entity.builder;

import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.model.JobGroupRequest;

import java.time.LocalDateTime;

import static com.navi.grabcodingexercise.util.DateTimeUtil.getCurrentTimeStamp;

/***
 * Builder class to create an instance of JobGroupInstance entity from JobGroupRequest model
 */
public class JobGroupInstanceBuilder {

    final JobGroupInstance instance;
    JobGroupRequest request;

    public JobGroupInstanceBuilder(JobGroupRequest request) {
        instance = new JobGroupInstance();
        this.request = request;
    }

    /***
     *  Main api method to trigger creation of JobGroupInstance Entity
     * @return An instance of {@link com.navi.grabcodingexercise.entity.JobGroupInstance}
     */
    public JobGroupInstance build() {
        instance.setGroupId(request.getGroupId());
        instance.setGroupInstanceId(request.getGroupId() + "-" + getCurrentTimeStamp());
        instance.setJobGroupSnapshot(request);
        instance.setStatus(ExecutionStatus.RUNNING);
        instance.setStartTime(LocalDateTime.now());

        return instance;
    }
}
