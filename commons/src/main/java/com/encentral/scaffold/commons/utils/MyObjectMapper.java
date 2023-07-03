package com.encentral.scaffold.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class MyObjectMapper {
    public ObjectMapper objectMapper = new ObjectMapper();

    MyObjectMapper(){
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String writeValueAsString(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    };

    public <T extends Object> T readValue(String content, Class<T> valueType) throws JsonProcessingException, IOException {
        return objectMapper.readValue(content, valueType);
    }
}
