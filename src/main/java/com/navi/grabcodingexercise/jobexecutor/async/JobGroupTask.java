package com.navi.grabcodingexercise.jobexecutor.async;

import com.navi.grabcodingexercise.entity.ExecutionStatus;
import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.entity.JobInstance;
import com.navi.grabcodingexercise.jobexecutor.JobCommandCreator;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.model.JobResult;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import com.navi.grabcodingexercise.repository.JobInstanceRepository;
import com.navi.grabcodingexercise.util.JsonConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.navi.grabcodingexercise.util.DateTimeUtil.getCurrentTimeStamp;

/***
 * A Callable task that represents a JobGroup. Contains the logic to execute a given Group
 */
public class JobGroupTask implements Callable<String> {
    final Logger logger = LoggerFactory.getLogger(JobGroupTask.class);
    private final JobGroupInstance jobGroupInstance;
    private final JobGroupRequest request;
    private final JobGroupInstanceRepository jobGroupInstanceRepository;
    private final JobInstanceRepository jobInstanceRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JobGroupTask(JobGroupInstance jobGroupInstance, JobGroupRequest request, JobGroupInstanceRepository jobGroupInstanceRepository, JobInstanceRepository jobInstanceRepository) {
        this.jobGroupInstance = jobGroupInstance;
        this.request = request;
        this.jobGroupInstanceRepository = jobGroupInstanceRepository;
        this.jobInstanceRepository = jobInstanceRepository;
    }

    @Override
    public String call() throws Exception {
        try{
            logger.info("Executing Job Group {}", request.getGroupId());
            List<JobGroupRequest.JobRequest> jobs = request.getJobs()
                    .stream()
                    .sorted(Comparator.comparingInt(JobGroupRequest.JobRequest::getRunId))
                    .collect(Collectors.toList());

            for(JobGroupRequest.JobRequest job : jobs){
                JobInstance instance = createJobInstance(job);

                String command = JobCommandCreator.create(job);
                //blocking for sequential execution
                JobResult result = executorService.submit(new JobTask(command)).get();
                logger.info("Result: {}", JsonConvertor.toJsonString(result));

                handleJobResult(result, instance, job);
            }
            updateJobGroupInstanceStatus(ExecutionStatus.COMPLETED);
            logger.info("Execution completed");
        }catch(Exception ex) {
            if(ex instanceof InterruptedException)
                logger.error("Job killed by external process");

            handleDanglingJob();
            updateJobGroupInstanceStatus(ExecutionStatus.FAILED);
            logger.error("Execution failed", ex);
        } finally {
            executorService.shutdownNow();
        }
        return jobGroupInstance.getGroupInstanceId();
    }

    private void handleDanglingJob() {
        Optional<JobInstance> maybeInstance = jobGroupInstance.getJobInstances().stream().filter(j -> j.getStatus().equals(ExecutionStatus.RUNNING)).findAny();
        maybeInstance.ifPresent( j -> j.setStatus(ExecutionStatus.FAILED));
    }

    private void handleJobResult(JobResult result, JobInstance instance, JobGroupRequest.JobRequest job) throws RuntimeException {
        instance.setJobResult(result);
        instance.setEndTime(LocalDateTime.now());

        if(!job.getContinueOnFail() && !result.getSuccess()){
            throw new RuntimeException(String.format("Job %s failed. Stopping group execution", job.getJobId()));
        }

        ExecutionStatus jobStatus = (result.getSuccess()) ? ExecutionStatus.COMPLETED : ExecutionStatus.FAILED;
        instance.setStatus(jobStatus);
        jobInstanceRepository.save(instance);
    }

    private void updateJobGroupInstanceStatus(ExecutionStatus status) {
        jobGroupInstance.setStatus(status);
        jobGroupInstance.setEndTime(LocalDateTime.now());
        jobGroupInstanceRepository.save(jobGroupInstance);
    }

    private JobInstance createJobInstance(JobGroupRequest.JobRequest job) {
        JobInstance instance = new JobInstance();
        instance.setJobGroupInstance(jobGroupInstance);
        instance.setJobId(job.getJobId());
        instance.setStatus(ExecutionStatus.RUNNING);
        instance.setStartTime(LocalDateTime.now());
        instance.setJobInstanceId(job.getJobId() + "-" + getCurrentTimeStamp());
        jobGroupInstance.getJobInstances().add(instance);

        return jobInstanceRepository.save(instance);
    }
}
