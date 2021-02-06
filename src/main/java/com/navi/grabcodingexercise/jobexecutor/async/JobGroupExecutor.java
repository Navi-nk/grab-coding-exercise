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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.navi.grabcodingexercise.util.DateTimeUtil.getCurrentTimeStamp;

public class JobGroupExecutor implements Callable<String> {
    final Logger logger = LoggerFactory.getLogger(JobGroupExecutor.class);
    private final JobGroupInstance jobGroupInstance;
    private final JobGroupRequest request;
    private final JobGroupInstanceRepository jobGroupInstanceRepository;
    private final JobInstanceRepository jobInstanceRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JobGroupExecutor(JobGroupInstance jobGroupInstance, JobGroupRequest request, JobGroupInstanceRepository jobGroupInstanceRepository, JobInstanceRepository jobInstanceRepository) {
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
                JobInstance instance = saveJobInstance(job);

                String command = new JobCommandCreator(job).create();
                JobResult result = executorService.submit(new JobExecutor(command)).get();
                logger.info("Result: {}", JsonConvertor.toJsonString(result));

                instance.setJobResult(result);
                instance.setStatus(ExecutionStatus.COMPLETED);
                jobInstanceRepository.save(instance);
            }
            updateJobGroupInstanceStatus(ExecutionStatus.COMPLETED);
            logger.info("Execution completed");
        }catch(Exception ex) {
            if(ex instanceof InterruptedException){
                logger.error("Job killed by external process", ex);
                executorService.shutdownNow();
            }
            logger.error("Error while executing job", ex);
            Optional<JobInstance> maybeInstance = jobGroupInstance.getJobInstances().stream().filter(j -> j.getStatus().equals(ExecutionStatus.RUNNING)).findAny();
            maybeInstance.ifPresent( j -> j.setStatus(ExecutionStatus.FAILED));
            updateJobGroupInstanceStatus(ExecutionStatus.FAILED);
        }
        return "";
    }

    private void updateJobGroupInstanceStatus(ExecutionStatus status) {
        jobGroupInstance.setStatus(status);
        jobGroupInstanceRepository.save(jobGroupInstance);
    }

    private JobInstance saveJobInstance(JobGroupRequest.JobRequest job) {
        JobInstance instance = new JobInstance();
        instance.setJobGroupInstance(jobGroupInstance);
        instance.setJobId(job.getJobId());
        instance.setStatus(ExecutionStatus.RUNNING);
        instance.setJobInstanceId(job.getJobId() + "-" + getCurrentTimeStamp());
        jobGroupInstance.getJobInstances().add(instance);

        return jobInstanceRepository.save(instance);
    }
}
