package com.unwulian.specification.exception;

import java.util.HashSet;
import java.util.Set;

public class GlobalException extends Exception {
    private final Set<String> errors = new HashSet<>();
    private final Set<String> warns = new HashSet<>();

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

    public boolean hasWarn(){
        return !warns.isEmpty();
    }

    public void addWarn(String error) {
        warns.add(error);
    }

    public void warn() {
        System.err.println("解析过程出现一些警告信息:"+warns);
    }
}
