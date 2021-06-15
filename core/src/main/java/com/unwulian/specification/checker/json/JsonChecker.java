package com.unwulian.specification.checker.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.unwulian.specification.checker.IFormatChecker;

public class JsonChecker implements IFormatChecker {

    @Override
    public boolean check(String content) {
        try {
            JsonElement parse = new JsonParser().parse(content);
            return parse.isJsonObject() || parse.isJsonArray();
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }


}
