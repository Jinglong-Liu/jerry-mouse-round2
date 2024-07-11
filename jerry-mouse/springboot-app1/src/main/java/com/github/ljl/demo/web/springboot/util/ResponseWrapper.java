package com.github.ljl.demo.web.springboot.util;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 13:51
 **/

@Data
public class ResponseWrapper {
    private Integer status = 200;
    private String message = "";
    private Map<String, Object> data = new HashMap<>();
    public ResponseWrapper() {}
    public ResponseWrapper addData(@NotNull Object object) {
        this.data.put(getDefaultName(object), object);
        return this;
    }
    public ResponseWrapper addData(String name, Object object) {
        if (this.data.containsKey(name)) {
            throw new RuntimeException("name " + name + "is in data know, using putData to replace");
        }
        this.data.put(name, object);
        return this;
    }
    public ResponseWrapper putData(String name ,Object object) {
        this.data.put(name, object);
        return this;
    }
    public ResponseWrapper putData(Object object) {
        this.data.put(getDefaultName(object), object);
        return this;
    }
    public ResponseWrapper setData(@NotNull Object object) {
        this.data.clear();
        this.data.put(getDefaultName(object), object);
        return this;
    }
    public ResponseWrapper setMessage(String message) {
        this.message = message;
        return this;
    }
    public ResponseWrapper setStatus(Integer status) {
        this.status = status;
        return this;
    }
    public static ResponseWrapper createBody() {
        return new ResponseWrapper();
    }

    public static ResponseWrapper ok() {
        return new ResponseWrapper();
    }

    public static ResponseWrapper unAuthorized() {
        return new ResponseWrapper().setStatus(401).setMessage("Unauthorized");
    }

    private static String deCapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return Character.toLowerCase(firstChar) + str.substring(1);
        }
        return str;
    }
    private static String getDefaultName(@NotNull Object object) {
        return deCapitalize(object.getClass().getSimpleName());
    }
}
