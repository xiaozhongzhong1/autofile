package com.unwulian.specification.parser.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unwulian.specification.parser.IParser;

public class JsonAttrParser implements IParser<String> {
    /**
     * 将json按照层级分布
     *
     * @param content
     * @return
     */
    @Override
    public String parse(String content) {
        StringBuilder builder = new StringBuilder();
        JsonObject json = new JsonParser().parse(content).getAsJsonObject();
        append(builder, json);
        return builder.toString().replaceAll("\"","");
    }

    private void append(StringBuilder builder, JsonObject json) {
        for (String key : json.keySet()) {
            JsonElement jsonElement = json.get(key);
            builder.append(key).append(LINE_SEPERATOR);

            if (jsonElement.isJsonObject()) {
                builder.append(EQUAL_KEY).append(key).append(LINE_SEPERATOR);
                append(builder, jsonElement.getAsJsonObject());
                builder.append(EQUAL_KEY).append(key).append(LINE_SEPERATOR);
            }else if(jsonElement.isJsonArray()){
                builder.append(EQUAL_KEY).append(key).append(LINE_SEPERATOR);
                JsonElement jsonElement1 = jsonElement.getAsJsonArray().get(0);
                append(builder, jsonElement1.getAsJsonObject());
                builder.append(EQUAL_KEY).append(key).append(LINE_SEPERATOR);
            }


        }
    }

}
