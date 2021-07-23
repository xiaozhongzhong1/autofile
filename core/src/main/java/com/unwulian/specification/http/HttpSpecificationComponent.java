package com.unwulian.specification.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.unwulian.specification.bean.ComponentParam;
import com.unwulian.specification.bean.TableBean;
import com.unwulian.specification.codemodel.MdModel;
import com.unwulian.specification.exception.GlobalException;
import com.unwulian.specification.parser.IParser;
import com.unwulian.specification.parser.json.JsonDictParser;
import com.unwulian.specification.utils.RegexUtil;

import java.util.*;
import java.util.stream.Collectors;

public class HttpSpecificationComponent {
    private HttpDictSpecification httpDictSpecification;
    private HttpRequestSpecification httpRequestSpecification = new HttpRequestSpecification();
    private HttpResponseSpecification httpResponseSpecification = new HttpResponseSpecification();
    private String dictReq;
    private String dictResp;
    private String request;
    private String response;
    private String title;
    private String type;
    private String[] dictReqAppend;
    private String[] dictRespAppend;
    private List<TableBean> commons = new ArrayList<>();

    public HttpSpecificationComponent(ComponentParam componentParam) {
        this.dictReq = componentParam.getDictReq();
        this.dictResp = componentParam.getDictResp();
        this.request = componentParam.getRequest();
        this.response = componentParam.getResponse();
        this.title = componentParam.getTitle();
        this.type = componentParam.getType();
        this.dictReqAppend = componentParam.getDictReqAppend();
        this.dictRespAppend = componentParam.getDictRespAppend();
        httpDictSpecification = new HttpDictSpecification(componentParam.getDictIndexes());
        commons.addAll(componentParam.getCommons());
    }

    public String[] getDictReqAppend() {
        return dictReqAppend;
    }

    public void setDictReqAppend(String... dictReqAppend) {
        this.dictReqAppend = dictReqAppend;
    }

    public String[] getDictRespAppend() {
        return dictRespAppend;
    }

    public void setDictRespAppend(String... dictRespAppend) {
        this.dictRespAppend = dictRespAppend;
    }

    private boolean check() {
        return httpDictSpecification.check(dictReq)
                && httpDictSpecification.check(dictResp)
                && httpRequestSpecification.check(request)
                && httpResponseSpecification.check(response);
    }

    public String parse() {
        if (!check()) {
            throw new RuntimeException("文件不规范，请检查文件内容");
        }

        List<TableBean> dictBeansReq = httpDictSpecification.parse(dictReq);
        appendTableBeans(dictBeansReq, dictReqAppend);

        List<TableBean> dictBeansResp = httpDictSpecification.parse(dictResp);
        appendTableBeans(dictBeansResp, dictRespAppend);

        String requestStr = httpRequestSpecification.parse(request);
        String responseStr = httpResponseSpecification.parse(response);

        try {
            String populateRequest = populateLine(dictBeansReq, requestStr);
            String populateResponse = populateLine(dictBeansResp, responseStr);
            String body = Joiner.on(System.lineSeparator()).join("request", populateRequest, "response", populateResponse);
            String mdContent = MdModel.ZD_MODEL.replace(MdModel.TITLE_PLACE_HOLDER, title)
                    .replace(MdModel.TYPE_PLACE_HOLDER, type)
                    .replace(MdModel.BODY_PLACE_HOLDER, body);
            return mdContent;
        } catch (Exception e) {
            GlobalException.getInstance().addError(type + " has no match :" + e.getMessage());
            return null;
        }

    }

    private void appendTableBeans(List<TableBean> dictBeansReq, String[] append) {
        if (null == append) {
            return;
        }
        Arrays.stream(append).forEach(s ->
                Optional.ofNullable(httpDictSpecification.parse(s))
                        .ifPresent(list -> dictBeansReq.addAll(list))
        );

    }

    private String populateLine(List<TableBean> dictBeans, String requestStr) {
        List<String> newLines = new ArrayList<>();
        String[] lines = requestStr.split(IParser.LINE_SEPERATOR);
        List<String> errors = new ArrayList<>();
        List<TableBean> append = new ArrayList<>();
        boolean appendFlag = false;
        String appendCache = null;
        Map<String,String> appendKeyMap = new HashMap<>();
        for (String line : lines) {
            if (line.contains(IParser.EQUAL_KEY)) {
                //已经存在了，则说明是结束标志
                if(line.equalsIgnoreCase(appendCache)){
                    appendFlag = false;
                    newLines.add(line.concat(""));
                    continue;
                }
                appendCache = line;
                newLines.add(line);
                //查看是否有泛型
                String appendKey = appendKeyMap.containsKey(line.substring(2))?
                        appendKeyMap.get(line.substring(2)):line.substring(2);
                append.clear();
                if (JsonManager.append.containsKey(appendKey)) {
                    List<TableBean> tableBeans = JsonDictParser.getTableBeans(JsonManager.append.get(appendKey));
                    if (CollUtil.isNotEmpty(tableBeans)) {
                        append.clear();
                        append.addAll(tableBeans);
                        appendFlag = true;
                    }
                }
                continue;
            }
            String key = line.trim();
            TableBean tableBean = null;
            /**
             * 先从给定的里面取，没有的话从append里面去取，再没有的话从common里面取
             */
            if (appendFlag) {
                try {
                    tableBean = getTableBean(key, append);
                } catch (Exception e1) {
                    try {
                        tableBean = getTableBean(key, commons);
                    } catch (Exception e2) {
                        errors.add(key);
                    }
                }
            } else {
                try {
                    tableBean = getTableBean(key, dictBeans);
                } catch (Exception e2) {
                    try{
                        tableBean = getTableBean(key, commons);
                    }catch (Exception e){
                        errors.add(key);
                    }

                }
            }

            String type = tableBean.getType();
            //查看是否有泛型
            String generalType = RegexUtil.getGeneralType(type);
            if(StrUtil.isNotEmpty(generalType)){
                appendKeyMap.put(key,generalType);
            }
            if (null != tableBean) {
                newLines.add(Joiner.on(":").join(line, type, tableBean.getComment()));
            }



        }
        if (errors.isEmpty()) {
            return newLines.stream().collect(Collectors.joining(System.lineSeparator()));
        }
        throw new RuntimeException(errors.stream().collect(Collectors.joining("|")));
    }


    private TableBean getTableBean(String key, List<TableBean> dictBeans) {
        try {
            return dictBeans.stream().filter(tableBean -> tableBean.getName().equalsIgnoreCase(key.trim()))
                    .findFirst().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
