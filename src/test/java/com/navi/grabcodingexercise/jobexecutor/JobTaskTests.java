package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.jobexecutor.async.JobTask;
import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.model.JobResult;
import com.navi.grabcodingexercise.util.JsonConvertor;
import org.junit.Assert;
import org.junit.Test;

public class JobTaskTests {

    @Test
    public void should_return_success_result() {
        JobTask executor = new JobTask("echo testMessage");
        JobResult actualResult = executor.call();
        JobResult expectedResult = createExpectedResult(true, "testMessage", null, null);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void should_return_failure_result() {
        JobTask executor = new JobTask("sh notfound.some");
        JobResult actualResult = executor.call();
        JobResult expectedResult = createExpectedResult(false, null,"sh: notfound.some: No such file or directory",  "Job failed with code: 127");
        Assert.assertEquals(expectedResult, actualResult);
    }

    private JobResult createExpectedResult(boolean status, String out,  String err, String ex) {
        JobResult expectedResult = new JobResult();
        expectedResult.setSuccess(status);
        expectedResult.setStandardOutMessage(out);
        expectedResult.setStandardErrorMessage(err);
        expectedResult.setExceptionMessage(ex);
        return expectedResult;
    }


}
