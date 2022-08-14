package com.example.aeon.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TemplateResponse {

    public Map templateSukses (Object object){
        Map map = new HashMap();
        map.put("data", object);
        map.put("message", "Sukses");
        map.put("status", "200");
        return map;
    }

    public Map templateSukses(Object object, String Message, String status){
        Map map = new HashMap();
        map.put("data", object);
        map.put("message", "Sukses");
        map.put("status", "200");
        return map;
    }

    public Map templateError(Object object){
        Map map = new HashMap();
        map.put("message", object);
        map.put("status", "404");
        return map;
    }

    public Map notFound(Object object){
        Map map = new HashMap();
        map.put("message", object);
        map.put("status", "404");
        return map;
    }

    public boolean checkNull(Object obj) {
        return obj == null;

    }
}
