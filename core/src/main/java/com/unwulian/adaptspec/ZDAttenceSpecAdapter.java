package com.unwulian.adaptspec;

import cn.hutool.core.io.FileUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 中鼎考勤机自适应http规范
 * 把不符合规范的转为符合规范的
 */
public class ZDAttenceSpecAdapter {

    private final String BASE_PATH = "C:\\Users\\Administrator\\Desktop\\testCode2\\";
    private final String DICT_PATH = BASE_PATH.concat("dict.txt");
    private final String REQUEST_PATH = BASE_PATH.concat("request.txt");
    private final String RESPONSE_PATH = BASE_PATH.concat("response.txt");

    private final List<String> dictList = Arrays.asList("loginRequest", "change_passwordRequest", "set_timeRequest", "get_timeResponse", "set_face_cfgRequest", "add_userRequest", "del_userRequest"
            , "query_userRequest", "query_userResponse", "remote_ctrlRequest", "upgradeRequest");
    private final List<String> requestList = Arrays.asList("login", "change_password", "set_time", "get_time", "heartbeat", "set_face_cfg",
            "add_user", "del_user", "query_user", "remote_ctrl", "upgrade", "restart");
    private final List<String> responseList = requestList;

    private final JsonArray dictArr;

    {
        String content = FileUtil.readString(new File(DICT_PATH), "utf-8");
        JsonElement parse = new JsonParser().parse(content);
        dictArr = parse.getAsJsonArray();
        System.out.println(dictArr);
    }

    /**
     * 生成request.txt的内容
     */
    public void generateRequestContent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String request : requestList) {
            JsonObject asJsonObject = new JsonObject();
            asJsonObject.addProperty("cmd", request);
            int dictIndex;
            JsonElement jsonElement;
            if ((dictIndex = dictList.indexOf(request.concat("Request"))) != -1
                    && (jsonElement = dictArr.get(dictIndex)) != null) {
                JsonArray asJsonArray = jsonElement.getAsJsonArray();
                JsonObject jsonObject = new JsonObject();
                for (int i = 1; i < asJsonArray.size(); i++) {
                    String key = asJsonArray.get(i).getAsJsonArray().get(0).getAsString();
                    jsonObject.add(key, null);
                }
                asJsonObject.add("data", jsonObject);
            }
            stringBuilder.append(asJsonObject.toString()).append(",").append(System.lineSeparator());
        }
        String requests = stringBuilder.toString();
        String substring = requests.trim().substring(0, requests.trim().length() - 1);
        String result = "[".concat(substring).concat("]");
        System.out.println(result);
        FileUtil.writeString(result,REQUEST_PATH,"utf-8");

    }

    /**
     * 生成response.txt的内容
     */
    public void generateResponseContent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String response : responseList) {
            JsonObject asJsonObject = new JsonObject();
            asJsonObject.addProperty("cmd", response.concat("_resp"));
            asJsonObject.addProperty("ret", 200);
            asJsonObject.addProperty("msg", "OK");
            int dictIndex;
            JsonElement jsonElement;
            if ((dictIndex = dictList.indexOf(response.concat("Response"))) != -1
                    && (jsonElement = dictArr.get(dictIndex)) != null) {
                JsonArray asJsonArray = jsonElement.getAsJsonArray();
                JsonObject jsonObject = new JsonObject();
                for (int i = 1; i < asJsonArray.size(); i++) {
                    String key = asJsonArray.get(i).getAsJsonArray().get(0).getAsString();
                    jsonObject.add(key, null);
                }
                asJsonObject.add("data", jsonObject);
            }
            stringBuilder.append(asJsonObject.toString()).append(",").append(System.lineSeparator());
        }
        String requests = stringBuilder.toString();
        String substring = requests.trim().substring(0, requests.trim().length() - 1);
        String result = "[".concat(substring).concat("]");
        System.out.println(result);
        FileUtil.writeString(result,RESPONSE_PATH,"utf-8");
    }

    public static void main(String[] args) {
        ZDAttenceSpecAdapter zdAttenceSpecAdapter = new ZDAttenceSpecAdapter();
        zdAttenceSpecAdapter.generateResponseContent();
        zdAttenceSpecAdapter.generateRequestContent();
    }

}
