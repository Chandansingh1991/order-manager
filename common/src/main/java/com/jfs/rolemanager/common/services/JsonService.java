package com.jfs.rolemanager.common.services;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class JsonService {
    private Gson gson = new Gson();

    public <T> String getJSONString(T objectToBeTransformed) {
        String jsonString = gson.toJson(objectToBeTransformed);
        return jsonString;
    }

    public <E> E getObjectFromJson(String json, Class<E> classType) {
        E jsonObject = gson.fromJson(json, classType);
        return jsonObject;
    }
}
