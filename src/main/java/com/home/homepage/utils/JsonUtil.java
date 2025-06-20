package com.home.homepage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

public class JsonUtil {
    public static String parse(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
