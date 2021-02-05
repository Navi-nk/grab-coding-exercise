package com.navi.grabcodingexercise.entity.builder;

import com.navi.grabcodingexercise.entity.Group;
import com.navi.grabcodingexercise.entity.Job;
import com.navi.grabcodingexercise.entity.JobType;
import com.navi.grabcodingexercise.model.JobGroupRequest;

import java.util.Set;
import java.util.stream.Collectors;

public class JobGroupBuilder {
    final Group jobGroup;
    JobGroupRequest request;

    public JobGroupBuilder(JobGroupRequest request) {
        jobGroup = new Group();
        withRequest(request);
    }

    public JobGroupBuilder(Group group, JobGroupRequest request){
        this.jobGroup = group;
        withRequest(request);
    }

    private void withRequest(JobGroupRequest request){
        this.request = request;
        //validateRequest();
    }

    public Group build() {
        jobGroup.setGroupId(request.getGroupId());
        jobGroup.setGroupDescription(request.getGroupDescription());
        jobGroup.setJobs(createJobs());
        return jobGroup;
    }

    private Set<Job> createJobs() {
        return request.getJobs().stream().map( j -> {
            Job job = new Job();
            job.setJobId(j.getJobId());
            job.setJobDescription(j.getJobDescription());
            job.setJobType(JobType.findJobType(j.getJobType()));
            job.setRunId(j.getRunId());
            job.setJarFile(j.getJarPath());
            job.setMainClass(j.getMainClass());
            job.setScriptFile(j.getScriptPath());
            job.setParams(j.getParams());
            job.setGroup(jobGroup);
            return job;
        }).collect(Collectors.toSet());
    }
}
