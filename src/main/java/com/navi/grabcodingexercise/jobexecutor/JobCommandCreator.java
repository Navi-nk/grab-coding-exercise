package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.entity.JobType;
import com.navi.grabcodingexercise.model.JobGroupRequest;

/***
 * Builder class to create an executable command from Job info.
 */
public class JobCommandCreator {

    /***
     * Constructs a executable command
     * @param job Contains Job info that is used to construct the corresponding command that will be executed
     * @return Command as String
     */
    public static String create(JobGroupRequest.JobRequest job) {
        JobType type = JobType.findJobType(job.getJobType());
        if(type == null)
            throw new IllegalArgumentException("invalid job type");

        String params = job.getParams() == null ? "" : job.getParams();
        switch (type) {
            case JAVA_PROCESS:
                return String.format("java -cp %s %s %s", job.getJarPath(), job.getMainClass(), params);
            case PYTHON_SCRIPT:
                return String.format("python %s %s", job.getScriptPath(), params);
            case SHELL_SCRIPT:
                return String.format("sh %s %s", job.getScriptPath(), params);
            default: throw new IllegalArgumentException("invalid job type");
        }
    }
}
