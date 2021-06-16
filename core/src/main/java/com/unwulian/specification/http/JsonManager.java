package com.unwulian.specification.http;

import cn.hutool.core.io.FileUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unwulian.specification.bean.ComponentParam;
import com.unwulian.specification.exception.GlobalException;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 提取json的信息，封装成可以生成文档的对象
 * 首先需要三个大json,字典，request,response
 * 每个request、response需要的字典有哪些（需要一个map）
 * 字典是按照我们的顺序排序的（可以编号）
 */
public class JsonManager {
    private JsonObject dict = new JsonObject();
    private JsonObject request = new JsonObject();
    private JsonObject response = new JsonObject();

    private List<String> requests = new ArrayList<>();
    private List<String> responses = new ArrayList<>();
    private List<String> dicts = new ArrayList<>();

    private Map<String, String[]> requestMap = new HashMap<>();
    private Map<String, String[]> responseMap = new HashMap<>();
    private Map<String, String> titleInfos = new HashMap<>();

    /**
     * 当request和response一一对应的时候，可以使用这个方法
     *
     * @param dict
     * @param request
     * @param response
     * @param dictSignals
     * @param reqSignals
     */
    public JsonManager(String dict, String request, String response, String[] dictSignals, String[] reqSignals) {
        this(dict, dictSignals, request, reqSignals, response, reqSignals);
    }

    public void setInfos(String[] types, String[] titles) {
        for (int i = 0; i < types.length; i++) {
            titleInfos.put(types[i], titles[i]);
        }
    }

    /**
     * @param dict           字典的json
     * @param dictSignals    每一个json的编号
     * @param request        请求的json
     * @param requestSignals 请求json每一个编号
     * @param response       响应的json
     * @param respSignals    响应json的每一个编号
     */
    public JsonManager(String dict, String[] dictSignals, String request, String[] requestSignals, String response, String[] respSignals) {
        JsonParser jsonParser = new JsonParser();

        JsonArray dictArr = jsonParser.parse(dict).getAsJsonArray();
        for (int i = 0; i < dictArr.size(); i++) {
            JsonArray asJsonArray = dictArr.get(i).getAsJsonArray();
            this.dict.add(dictSignals[i], asJsonArray);
        }
        JsonArray requestArr = jsonParser.parse(request).getAsJsonArray();
        for (int i = 0; i < requestArr.size(); i++) {
            JsonObject asJsonObject = requestArr.get(i).getAsJsonObject();
            this.request.add(requestSignals[i], asJsonObject);
        }
        JsonArray respArr = jsonParser.parse(response).getAsJsonArray();
        for (int i = 0; i < respArr.size(); i++) {
            JsonObject asJsonObject = respArr.get(i).getAsJsonObject();
            this.response.add(respSignals[i], asJsonObject);
        }

        requests.addAll(Arrays.stream(requestSignals).collect(Collectors.toList()));
        responses.addAll(Arrays.stream(respSignals).collect(Collectors.toList()));
        dicts.addAll(Arrays.stream(dictSignals).collect(Collectors.toList()));

        initMap();

    }

    /**
     * 通过上面的信息填充两个map
     */
    private void initMap() {
        for (String req : requests) {
            if (dicts.contains(req.concat("Request"))) {
                requestMap.put(req, new String[]{req + "Request"});
                continue;
            }
            throw new RuntimeException("request : " + req + "do not have dict");
        }
        for (String resp : responses) {
            if (dicts.contains(resp.concat("Request"))) {
                responseMap.put(resp, new String[]{resp + "Response"});
                continue;
            }
            throw new RuntimeException("response : " + resp + "do not have dict");
        }
    }

    public void editRequestMap(String request, String... dict) {
        requestMap.put(request, dict);
    }

    public void editResponseMap(String response, String... dict) {
        responseMap.put(response, dict);
    }


    public String parse() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String request : requests) {
            ComponentParam componentParam = new ComponentParam();
            componentParam.setRequest(this.request.get(request).getAsJsonObject().toString());
            componentParam.setResponse(this.response.get(request).getAsJsonObject().toString());
            String[] requestDicts = requestMap.get(request);
            String[] respDicts = responseMap.get(request);
            try {
                componentParam.setDictReq(this.dict.get(requestDicts[0]).getAsJsonArray().toString());
                componentParam.setDictResp(this.dict.get(respDicts[0]).getAsJsonArray().toString());
            }catch (Exception e){
                System.out.println(e);
            }
            if (requestDicts.length > 1) {
                String[] arr = new String[requestDicts.length-1];
                for (int i = 1; i < requestDicts.length; i++) {
                    arr[i - 1] = this.dict.get(requestDicts[i]).getAsJsonArray().toString();
                }
                componentParam.setDictReqAppend(arr);
            }
            if (respDicts.length > 1) {
                String[] arr = new String[respDicts.length-1];
                for (int i = 1; i < respDicts.length; i++) {
                    arr[i - 1] = this.dict.get(respDicts[i]).getAsJsonArray().toString();
                }
                componentParam.setDictRespAppend(arr);
            }
            componentParam.setType(request);
            componentParam.setTitle(titleInfos.get(request));

            String parse = new HttpSpecificationComponent(componentParam).parse();
            if(null!=parse) {
                stringBuilder.append(parse).append(System.lineSeparator());
            }
        }

        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        String dictPath = "C:\\Users\\Administrator\\Desktop\\testCode\\dict.txt";
        String reqPath = "C:\\Users\\Administrator\\Desktop\\testCode\\request.txt";
        String respPath = "C:\\Users\\Administrator\\Desktop\\testCode\\response.txt";
        List<String> dictSigs = new ArrayList<>();
        String[] reqSigs = {"deviceInfo","IOControl","timeSynchronization","restart","upgradeDevice","getHTTPParameters","setHTTPParameters","addPerson","editPerson","deletePerson"};
        for (int i = 0; i < reqSigs.length; i++) {
            dictSigs.add(reqSigs[i].concat("Request"));
            dictSigs.add(reqSigs[i].concat("Response"));
        }
        dictSigs.add("personInfo");
        String dict = FileUtil.readString(new File(dictPath), "utf-8");
        String req = FileUtil.readString(new File(reqPath), "utf-8");
        String resp = FileUtil.readString(new File(respPath), "utf-8");
        JsonManager jsonManager = new JsonManager(dict,req,resp,dictSigs.toArray(new String[dictSigs.size()]),reqSigs);
        jsonManager.editRequestMap("addPerson","addPersonRequest","personInfo");
        jsonManager.editRequestMap("editPerson","editPersonRequest","personInfo");
        jsonManager.editResponseMap("addPerson","addPersonResponse","personInfo");
        jsonManager.editResponseMap("editPerson","editPersonResponse","personInfo");
        jsonManager.setInfos(new String[]{"deviceInfo","IOControl","timeSynchronization","restart","upgradeDevice","getHTTPParameters","setHTTPParameters","addPerson","editPerson","deletePerson"},
                new String[]{"设备信息","远程开门","时钟同步","设备重启","设备升级","获取http参数","修改http参数","添加人员","修改人员","删除人员"});
        String parse = jsonManager.parse();
        GlobalException instance = GlobalException.getInstance();
        if(instance.hasError()) {
            instance.error();
        }
        System.out.println(parse);
    }

}
