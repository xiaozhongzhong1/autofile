package com.unwulian.specification.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 表格中列对含义
 */
public class TableMap {
    private Map<String, Integer> map = new HashMap<>();
    private static final String NAME = "name";
    private static final String COMMENT = "comment";
    private static final String TYPE = "type";

    public void put(Integer... index) {
        map.put(NAME, index[0]);
        map.put(COMMENT, index[1]);
        map.put(TYPE, index[2]);
    }

    public Integer getNameIndex() {
        return map.get(NAME);
    }

    public Integer getCommentIndex() {
        return map.get(COMMENT);
    }

    public Integer getTypeIndex() {
        return map.get(TYPE);
    }
}
