package com.navi.grabcodingexercise.entity.builder;

import com.navi.grabcodingexercise.entity.Group;
import com.navi.grabcodingexercise.entity.Job;
import com.navi.grabcodingexercise.entity.JobType;
import com.navi.grabcodingexercise.model.JobGroupRequest;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.navi.grabcodingexercise.model.JobGroupRequest.*;

/***
 * Builder class to create an instance of Group Entity from JobGroupRequest model. This also is responsible to perform any validation on the request
 */
public class JobGroupBuilder {
    final Group jobGroup;
    JobGroupRequest request;

    public JobGroupBuilder(JobGroupRequest request) {
        jobGroup = new Group();
        withRequest(request);
    }

    public JobGroupBuilder(Group group, JobGroupRequest request) {
        this.jobGroup = group;
        withRequest(request);
    }

    private void withRequest(JobGroupRequest request) {
        this.request = request;
        validateRequest();
    }

    /***
     * Main api method to trigger creation of Group Entity
     * @return An instance of {@link com.navi.grabcodingexercise.entity.Group}.
     */
    public Group build() {
        jobGroup.setGroupId(request.getGroupId());
        jobGroup.setGroupDescription(request.getGroupDescription());
        jobGroup.setJobs(createJobs());
        return jobGroup;
    }

    private Set<Job> createJobs() {
        return request.getJobs().stream().map(j -> {
            Job job = new Job();
            job.setJobId(j.getJobId());
            job.setJobDescription(j.getJobDescription());
            job.setJobType(JobType.findJobType(j.getJobType()));
            job.setRunId(j.getRunId());
            job.setJarFile(j.getJarPath());
            job.setMainClass(j.getMainClass());
            job.setScriptFile(j.getScriptPath());
            job.setParams(j.getParams());
            job.setContinueOnFail(j.getContinueOnFail());
            job.setGroup(jobGroup);
            return job;
        }).collect(Collectors.toSet());
    }

    private void validateRequest() {
        Objects.requireNonNull(request.getGroupId(), "Group Id cannot be Null");
        validateJobs();
    }

    private void validateJobs() {

        checkInputPredicate((Set<JobRequest> j) -> j == null || j.size() == 0,
                request.getJobs(),
                "Jobs request cannot be empty");

        Set<Integer> providedRunIds = new HashSet<>();
        request.getJobs().forEach(j -> {
            Objects.requireNonNull(j.getJobId(), "Job Id cannot be null");
            Objects.requireNonNull(j.getRunId(), "run id cannot be null");
            Objects.requireNonNull(j.getJobType(), "Job Type cannot be null");

            checkInputPredicate((String type) -> JobType.findJobType(type) == null,
                    j.getJobType(),
                    "JobType not found for:" + j.getJobType());

            checkInputPredicate(providedRunIds::contains,
                    j.getRunId(),
                    "Jobs must have unique run ids");

            providedRunIds.add(j.getRunId());

            checkJobTypeArgs(j);

        });
    }

    private void checkJobTypeArgs(JobRequest j) {
        JobType jobType = JobType.findJobType(j.getJobType());
        switch (jobType) {
            case JAVA_PROCESS:
                checkInputPredicate((JobRequest r) -> r.getJarPath() == null || r.getMainClass() == null,
                        j, "Java process args jarPath & mainClass are needed");
                break;
            case PYTHON_SCRIPT:
            case SHELL_SCRIPT:
                Objects.requireNonNull(j.getScriptPath(), "Script path is needed for provided JobType");
        }
    }

    private <T> void checkInputPredicate(Predicate<T> check, T input, String message) {
        if (check.test(input)) {
            throw new IllegalArgumentException(message);
        }
    }


}
