package com.navi.grabcodingexercise.entity.convertor;

import com.navi.grabcodingexercise.model.JobResult;
import com.navi.grabcodingexercise.util.JsonConvertor;

import javax.persistence.AttributeConverter;

public class JobResultConvertor implements
        AttributeConverter<JobResult, String> {

    @Override
    public String convertToDatabaseColumn(JobResult jobResult) {
        return JsonConvertor.toJsonString(jobResult);
    }

    @Override
    public JobResult convertToEntityAttribute(String s) {
        return JsonConvertor.toObject(s, JobResult.class);
    }
}
