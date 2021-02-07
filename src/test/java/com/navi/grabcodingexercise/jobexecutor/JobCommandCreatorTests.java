package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.entity.JobType;
import org.junit.Assert;
import org.junit.Test;

import static com.navi.grabcodingexercise.model.JobGroupRequest.JobRequest;

public class JobCommandCreatorTests {
    @Test
    public void should_create_java_command(){
        String command = JobCommandCreator.create(createJavaJob());
        Assert.assertEquals("java -cp /some.jar com.some.Main 1", command);
    }

    @Test
    public void should_create_shell_command(){
        String command = JobCommandCreator.create(createShellJob());
        Assert.assertEquals("sh /some.sh 1", command);
    }

    @Test
    public void should_create_python_command(){
        String command = JobCommandCreator.create(createPythonJob());
        Assert.assertEquals("python /some.py 1", command);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_for_invalid_job() {
        JobRequest j = new JobRequest();
        j.setJobType("invalid");
        JobCommandCreator.create(j);
    }

    private JobRequest createJavaJob(){
        JobRequest j = new JobRequest();
        j.setJobType(JobType.JAVA_PROCESS.getJobTypeValue());
        j.setJarPath("/some.jar");
        j.setMainClass("com.some.Main");
        j.setParams("1");
        return j;
    }

    private JobRequest createPythonJob(){
        JobRequest j = new JobRequest();
        j.setJobType(JobType.PYTHON_SCRIPT.getJobTypeValue());
        j.setScriptPath("/some.py");
        j.setParams("1");
        return j;
    }

    private JobRequest createShellJob(){
        JobRequest j = new JobRequest();
        j.setJobType(JobType.SHELL_SCRIPT.getJobTypeValue());
        j.setScriptPath("/some.sh");
        j.setParams("1");
        return j;
    }
}
