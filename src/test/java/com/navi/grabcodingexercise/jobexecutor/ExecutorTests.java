package com.navi.grabcodingexercise.jobexecutor;

import com.navi.grabcodingexercise.util.JsonConvertor;
import org.junit.Test;

public class ExecutorTests {

    @Test
    public void test() throws Exception{
        JobExecutor executor = new JobExecutor();
        System.out.println(JsonConvertor.toJsonString(executor.execute("ls -lrt; ls")));
    }
}
