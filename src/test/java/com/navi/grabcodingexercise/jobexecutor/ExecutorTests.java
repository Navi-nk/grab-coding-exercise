package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.jobexecutor.async.JobTask;
import com.navi.grabcodingexercise.util.JsonConvertor;
import org.junit.Test;

public class ExecutorTests {

    @Test
    public void test() throws Exception {
        //JobExecutor executor = new JobExecutor("java -jar /Users/navi-mac/Desktop/workspace/grab-coding-exercise/example/target/java-example-0.0.1-SNAPSHOT.jar 1");
        JobTask executor = new JobTask("sleep 10");
        System.out.println(JsonConvertor.toJsonString(executor.call()));
    }
}
