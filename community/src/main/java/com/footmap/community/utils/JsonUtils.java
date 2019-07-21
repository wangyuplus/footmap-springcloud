package com.footmap.community.utils;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class JsonUtils {

    public static Map<String ,String> strToMap(String jsonStr) {
        return (Map<String, String>) JSON.parse(jsonStr);
    }
}
