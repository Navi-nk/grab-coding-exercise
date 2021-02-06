package com.navi.grabcodingexercise.jobexecutor.async;

import com.navi.grabcodingexercise.entity.JobGroupInstance;
import com.navi.grabcodingexercise.jobexecutor.JobCommandCreator;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.model.JobResult;
import com.navi.grabcodingexercise.repository.JobGroupInstanceRepository;
import com.navi.grabcodingexercise.util.JsonConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class JobGroupExecutor implements Callable<String> {//implements Supplier<String> {
    final Logger logger = LoggerFactory.getLogger(JobGroupExecutor.class);
    private final JobGroupInstance instance;
    private final JobGroupRequest request;
    private final JobGroupInstanceRepository jobGroupInstanceRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JobGroupExecutor(JobGroupInstance instance, JobGroupRequest request, JobGroupInstanceRepository jobGroupInstanceRepository) {
        this.instance = instance;
        this.request = request;
        this.jobGroupInstanceRepository = jobGroupInstanceRepository;
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
                String command = new JobCommandCreator(job).create();
                JobResult result = executorService.submit(new JobExecutor(command)).get();
                logger.info("Result: {}", JsonConvertor.toJsonString(result));
            }
            logger.info("Execution completed");
        }catch(Exception ex) {
            if(ex instanceof InterruptedException){
                logger.error("Some thing happened", ex);
                executorService.shutdownNow();
            }

        }
        return "";
    }

  /*  @Override
    public String get() {
        try{
            TimeUnit.MINUTES.sleep(15);
            logger.info("completed inside now");
        }catch(Exception ex) {
            logger.error("Some thing happened");
        }
        return "";
    }*/
}
