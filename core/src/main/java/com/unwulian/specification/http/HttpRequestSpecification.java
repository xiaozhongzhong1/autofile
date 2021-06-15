package com.unwulian.specification.http;

import com.unwulian.specification.IInputSpecification;
import com.unwulian.specification.checker.json.JsonChecker;
import com.unwulian.specification.parser.json.JsonAttrParser;

public class HttpRequestSpecification implements IInputSpecification {
    private final JsonChecker jsonChecker = new JsonChecker();
    private final JsonAttrParser jsonAttrParser = new JsonAttrParser();
    @Override
    public boolean check(String content) {
        return jsonChecker.check(content);
    }

    @Override
    public String parse(String content) {
        return jsonAttrParser.parse(content);
    }
}
