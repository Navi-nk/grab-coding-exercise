package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.jobexecutor.async.ProcessStreamHandler;
import com.navi.grabcodingexercise.model.JobResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JobExecutor {
    private final int SUCCESS_EXIT_CODE = 0;
    private final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    public JobResult execute(String command) {
        JobResult result = new JobResult();
        List<String> stdOut = new ArrayList<>();
        List<String> stdErr = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(command);
            ProcessStreamHandler stdOutHandler = new ProcessStreamHandler(process.getInputStream(), stdOut::add,"stdOut");
            ProcessStreamHandler stdErrHandler = new ProcessStreamHandler(process.getErrorStream(), stdErr::add,"stdErr");

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.submit(stdOutHandler);
            executor.submit(stdErrHandler);

            int exitCode = process.waitFor();

            executor.shutdown();
            executor.awaitTermination(1L, TimeUnit.HOURS);

            if(stdErr.size() > 0)
                result.setStandardErrorMessage(String.join("\n", stdErr));
            if(stdOut.size() > 0)
                result.setStandardOutMessage(String.join("\n", stdOut));
            if(exitCode != 0)
                result.setExceptionMessage("Job failed with code: " + exitCode);
            result.setSuccess(exitCode == SUCCESS_EXIT_CODE);

            return result;
        } catch (Exception e){
            logger.error(String.format("Exception while executing job : %s", command), e);
            result.setSuccess(false);
            result.setExceptionMessage(e.getMessage());
            return result;
        }
    }
}
