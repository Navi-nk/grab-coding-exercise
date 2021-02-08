package com.navi.grabcodingexercise.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/***
 * Class to serialise and de-serialise a given object. Uses Jackson object mapper for achieving this.
 */
public class JsonConvertor {
    public static <T> T toObject(String jsonString, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Error while deserializing json string", e);
        }
    }

    public static <T> String toJsonString(T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Error while serializing object", e);
        }
    }
}
