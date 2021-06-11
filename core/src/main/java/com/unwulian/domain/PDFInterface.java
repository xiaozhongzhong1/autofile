package com.unwulian.domain;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zzxu
 * @date: 2021/6/2 11:23
 * @description:
 */
public class PDFInterface implements Serializable {

    private String operator;

    private String title;

    private String uri;

    private List<PDFInterfacceField> fieldList;

    private List<PDFInterfacceField> resFieldList;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<PDFInterfacceField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<PDFInterfacceField> fieldList) {
        this.fieldList = fieldList;
    }

    public List<PDFInterfacceField> getResFieldList() {
        return resFieldList;
    }

    public void setResFieldList(List<PDFInterfacceField> resFieldList) {
        this.resFieldList = resFieldList;
    }

    @Override
    public String toString() {
        return "PDFInterface{" +
                "operator='" + operator + '\'' +
                ", title='" + title + '\'' +
                ", uri='" + uri + '\'' +
                ", fieldList=" + fieldList +
                '}';
    }
}
