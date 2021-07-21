package com.unwulian.specification.http;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Joiner;
import com.unwulian.specification.bean.ComponentParam;
import com.unwulian.specification.bean.TableBean;
import com.unwulian.specification.codemodel.MdModel;
import com.unwulian.specification.exception.GlobalException;
import com.unwulian.specification.parser.IParser;
import com.unwulian.specification.parser.json.JsonDictParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        for (String line : lines) {
            if (line.contains(IParser.EQUAL_KEY)) {
                newLines.add(line);
                String appendKey = line.substring(2);
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
            try {
                tableBean = getTableBean(key, dictBeans);
            } catch (Exception e) {
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
                        tableBean = getTableBean(key, commons);
                    } catch (Exception e2) {
                        errors.add(key);
                    }
                }
            }
            if (null != tableBean) {
                newLines.add(Joiner.on(":").join(line, tableBean.getType(), tableBean.getComment()));
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
