package com.unwulian.specification.http;

import com.unwulian.specification.IOutputSpecification;
import com.unwulian.specification.bean.TableBean;
import com.unwulian.specification.checker.json.JsonChecker;
import com.unwulian.specification.parser.json.JsonAttrParser;
import com.unwulian.specification.parser.json.JsonDictParser;

import java.util.List;

public class HttpResponseSpecification implements IOutputSpecification {
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
