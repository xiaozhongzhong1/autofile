package com.unwulian.specification.parser.json;

import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.unwulian.specification.bean.TableBean;
import com.unwulian.specification.bean.TableMap;
import com.unwulian.specification.parser.IParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典
 * 用于查找字段的注释与类型
 * 从table生成的json获取
 */
public class JsonDictParser implements IParser<List<TableBean>> {
    private TableMap tableMap = new TableMap();

    public JsonDictParser(Integer... indexes) {
        tableMap.put(indexes);
    }

    @Override
    public List<TableBean> parse(String content) {
        if(StrUtil.isEmpty(content)){
            return null;
        }
        List<TableBean> list = new ArrayList<>();
        JsonArray asJsonArray = new JsonParser().parse(content).getAsJsonArray();
        //删除第一个
        asJsonArray.remove(0);
        for (JsonElement jsonElement : asJsonArray) {
            TableBean tableBean = new TableBean(jsonElement.getAsJsonArray(), tableMap);
            list.add(tableBean);
        }
        return list;
    }
}
