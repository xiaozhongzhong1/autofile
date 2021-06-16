package com.unwulian.specification.bean;

/**
 * 完成解析需要的参数集合
 */
public class ComponentParam {
    private String dictReq;
    private String dictResp;
    private String request;
    private String response;
    private String title;
    private String type;
    private String[] dictReqAppend;
    private String[] dictRespAppend;

    public ComponentParam() {
    }

    public ComponentParam(String dictReq, String dictResp, String request, String response, String title, String type) {
        this.dictReq = dictReq;
        this.dictResp = dictResp;
        this.request = request;
        this.response = response;
        this.title = title;
        this.type = type;
    }

    public ComponentParam(String dictReq, String dictResp, String request, String response, String title, String type, String[] dictReqAppend, String[] dictRespAppend) {
        this.dictReq = dictReq;
        this.dictResp = dictResp;
        this.request = request;
        this.response = response;
        this.title = title;
        this.type = type;
        this.dictReqAppend = dictReqAppend;
        this.dictRespAppend = dictRespAppend;
    }

    public String getDictReq() {
        return dictReq;
    }

    public void setDictReq(String dictReq) {
        this.dictReq = dictReq;
    }

    public String getDictResp() {
        return dictResp;
    }

    public void setDictResp(String dictResp) {
        this.dictResp = dictResp;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getDictReqAppend() {
        return dictReqAppend;
    }

    public void setDictReqAppend(String[] dictReqAppend) {
        this.dictReqAppend = dictReqAppend;
    }

    public String[] getDictRespAppend() {
        return dictRespAppend;
    }

    public void setDictRespAppend(String[] dictRespAppend) {
        this.dictRespAppend = dictRespAppend;
    }
}
