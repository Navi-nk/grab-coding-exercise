package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.entity.JobType;
import com.navi.grabcodingexercise.model.JobGroupRequest;

public class JobCommandCreator {

    private final JobGroupRequest.JobRequest job;

    public JobCommandCreator(JobGroupRequest.JobRequest job) {
        this.job = job;
    }

    public String create() {
        JobType type = JobType.findJobType(job.getJobType());
        String params = job.getParams() == null ? "" : job.getParams();
        switch (type) {
            case JAVA_PROCESS:
                return String.format("java -cp %s %s %s ", job.getJarPath(), job.getMainClass(), params);
            case PYTHON_SCRIPT:
                return String.format("python %s %s", job.getScriptPath(), params);
            case SHELL_SCRIPT:
                return String.format("sh %s %s", job.getScriptPath(), params);
            default: throw new IllegalArgumentException("invalid job type");
        }
    }
}
