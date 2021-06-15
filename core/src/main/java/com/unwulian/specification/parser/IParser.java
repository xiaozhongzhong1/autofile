package com.unwulian.specification.parser;

public interface IParser<T> {
    String POUND_KEY = "#";
    String LINE_SEPERATOR = System.lineSeparator();

    T parse(String content);
}
