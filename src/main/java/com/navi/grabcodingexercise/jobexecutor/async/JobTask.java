package com.navi.grabcodingexercise.jobexecutor.async;

import com.navi.grabcodingexercise.model.JobResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/***
 * A Callable task that represents a Job. Contains the logic to execute a Job of a given Group
 */
public class JobTask implements Callable<JobResult> {

    private final int SUCCESS_EXIT_CODE = 0;
    private final Logger logger = LoggerFactory.getLogger(JobTask.class);

    final String command;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    public JobTask(String command) {
        this.command = command;
    }

    @Override
    public JobResult call() {
        JobResult result = new JobResult();
        List<String> stdOut = new ArrayList<>();
        List<String> stdErr = new ArrayList<>();
        try {
            logger.info("Executing command {}", command);
            Process process = Runtime.getRuntime().exec(command);
            ProcessStreamHandler stdOutHandler = new ProcessStreamHandler(process.getInputStream(), stdOut::add,"stdOut");
            ProcessStreamHandler stdErrHandler = new ProcessStreamHandler(process.getErrorStream(), stdErr::add,"stdErr");

            Future<?> sf = executor.submit(stdOutHandler);
            Future<?> ef = executor.submit(stdErrHandler);

            int exitCode = process.waitFor();
            while (!sf.isDone() || !ef.isDone());

            handleOutErrStreams(result, stdOut, stdErr);
            if(exitCode != 0)
                result.setExceptionMessage("Job failed with code: " + exitCode);
            result.setSuccess(exitCode == SUCCESS_EXIT_CODE);
            logger.info("Finished command {}", command);
            return result;
        } catch (Exception e){
            logger.error(String.format("Exception while executing job : %s", command), e);
            result.setSuccess(false);
            result.setExceptionMessage(e.getMessage());
            handleOutErrStreams(result, stdOut, stdErr);
            return result;
        }finally {
            logger.info("Shutting down stream collector executor");
            executor.shutdownNow();
        }
    }

    private void handleOutErrStreams(JobResult result, List<String> stdOut, List<String> stdErr) {
        if(stdErr.size() > 0)
            result.setStandardErrorMessage(String.join("\n", stdErr));
        if(stdOut.size() > 0)
            result.setStandardOutMessage(String.join("\n", stdOut));
    }
}
