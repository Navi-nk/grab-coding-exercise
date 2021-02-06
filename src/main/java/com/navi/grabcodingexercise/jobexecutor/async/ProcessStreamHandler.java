package com.navi.grabcodingexercise.jobexecutor.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class ProcessStreamHandler implements Runnable {
    private Logger logger = LoggerFactory.getLogger(ProcessStreamHandler.class);
    private InputStream is;
    private Consumer<String> out;
    private String streamType;

    public ProcessStreamHandler(InputStream is, Consumer<String> out, String streamType) {
        this.is = is;
        this.out = out;
        this.streamType = streamType;
    }

    @Override
    public void run() {
        try {
            logger.info("Starting reading from process stream:" + streamType);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            reader.lines().forEach(out);
            logger.info("completed reading from process stream:" + streamType);
        }
        catch(Exception e) {
            logger.error("Error while reading from process stream", e);
        }
    }
}
