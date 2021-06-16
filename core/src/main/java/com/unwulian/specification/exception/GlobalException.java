package com.unwulian.specification.exception;

import java.util.HashSet;
import java.util.Set;

public class GlobalException extends Exception {
    private final Set<String> errors = new HashSet<>();

    private GlobalException() {
    }

    private static GlobalException instance = new GlobalException();

    public static GlobalException getInstance() {
        return instance;
    }

    public boolean hasError(){
        return !errors.isEmpty();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void error() {
        System.err.println(errors);
        throw new RuntimeException("解析存在错误，请检查相关参数");
    }
}
