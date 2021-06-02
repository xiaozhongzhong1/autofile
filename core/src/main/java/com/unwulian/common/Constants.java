package com.unwulian.common;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONArray;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.unwulian.domain.PDFInterfacceField;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zzxu
 * @date: 2021/6/2 11:55
 * @description:
 */
public class Constants {

    public final static String START_LINE = "下发 body";

    public final static String INTERFACE_KEY = "Operator:";

    public final static String INTERFACE_URI = "URI:";


    public final static String BODY_IS_CN = "[\\\\u4e00-\\\\u9fa5]+";

    public final static String BODY_IS_MARTH = "[0-9].*";

    public final static String BODY_SYMBOL_POINT = ".";

    public final static String BODY_SYMBOL_COLON = ":";

    public final static String BODY_SYMBOL_URI_SPLIT = "<server ipaddr>";

    public static String TABLE_PATH = "F:\\tables.txt";

    public static Map<String, PDFInterfacceField> fieldMap = new HashMap<>();

    static {
        FileReader fileReader = new FileReader(TABLE_PATH);
        String result = fileReader.readString();
        Gson gson = new Gson();
        JSONArray jsonArray = gson.fromJson(result,JSONArray.class);
        jsonArray.stream().forEach(e ->{
            JSONArray line = gson.fromJson(e.toString().replace("\\n",""),JSONArray.class);
            line.stream().forEach(l ->{
                PDFInterfacceField field = new PDFInterfacceField();
                JSONArray fieldJson = gson.fromJson(l.toString(),JSONArray.class);
                field.setName(fieldJson.get(0).toString());
                field.setType(fieldJson.get(1).toString());
                field.setRange(fieldJson.get(2).toString());
                field.setRemark(fieldJson.get(3).toString());
                fieldMap.put(field.getName(),field);
            });
        });
        fieldMap.remove("Key");
        fieldMap.remove("operator");
        fieldMap.remove("info");
    }

}
