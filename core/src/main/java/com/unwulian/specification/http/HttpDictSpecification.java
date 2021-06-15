package com.unwulian.specification.http;

import com.unwulian.specification.IDictSpecification;
import com.unwulian.specification.bean.TableBean;
import com.unwulian.specification.checker.json.JsonChecker;
import com.unwulian.specification.parser.json.JsonDictParser;

import java.util.List;

public class HttpDictSpecification implements IDictSpecification {
    private final JsonChecker jsonChecker = new JsonChecker();
    private final JsonDictParser jsonDictParser = new JsonDictParser(0,1,4);

    @Override
    public boolean check(String content) {
        return jsonChecker.check(content);
    }

    @Override
    public List<TableBean> parse(String content) {
        return jsonDictParser.parse(content);
    }
}
