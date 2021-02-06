package com.navi.grabcodingexercise.entity.convertor;

import com.navi.grabcodingexercise.model.JobGroupRequest;
import com.navi.grabcodingexercise.util.JsonConvertor;

import javax.persistence.AttributeConverter;

public class JobGroupAttributeConvertor implements
        AttributeConverter<JobGroupRequest, String> {

    @Override
    public String convertToDatabaseColumn(JobGroupRequest jobGroupRequest) {
        return JsonConvertor.toJsonString(jobGroupRequest);
    }

    @Override
    public JobGroupRequest convertToEntityAttribute(String s) {
        return JsonConvertor.toObject(s, JobGroupRequest.class);
    }
}
