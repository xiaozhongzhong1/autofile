package com.unwulian.specification.bean;

import cn.hutool.core.util.StrUtil;
import com.unwulian.specification.anno.XpathNode;
import com.unwulian.specification.utils.XPathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class XmlBean {
    @XpathNode("doc/content/dictPath")
    private String dictPath;
    @XpathNode("doc/content/reqPath")
    private String reqPath;
    @XpathNode("doc/content/respPath")
    private String respPath;
    @XpathNode("doc/sigs/dictSigs")
    private String dictSigs;
    @XpathNode("doc/sigs/reqSigs")
    private String reqSigs;
    @XpathNode("doc/titles")
    private String titles;
    @XpathNode("doc/commonTableBeans/names")
    private String commonNames;
    @XpathNode("doc/commonTableBeans/comments")
    private String commonComments;
    @XpathNode("doc/commonTableBeans/types")
    private String commonTypes;
    @XpathNode("doc/dictIndexes")
    private String dictIndexes;

    public List<TableBean> tableBeans() {
        List<TableBean> list = null;
        if (StrUtil.isEmpty(commonNames)) {
            return list;
        }
        String[] names = split(commonNames);
        String[] comments = split(commonComments);
        String[] types = split(commonTypes);
        for (int i = 0; i < names.length; i++) {
            if (list == null) {
                list = new ArrayList<>();
            }
            TableBean tableBean = new TableBean(names[i], comments[i], types[i]);
            list.add(tableBean);
        }
        return list;
    }

    private String[] split(String commonNames) {
        List<String> collect = Arrays.stream(commonNames.split(","))
                .map(str -> str.trim()).collect(Collectors.toList());
        return collect.toArray(new String[collect.size()]);
    }

    public Integer[] dictIndexes() {
        List<Integer> indexes = Arrays.stream(split(dictIndexes)).map(str -> Integer.parseInt(str))
                .collect(Collectors.toList());
        return indexes.toArray(new Integer[indexes.size()]);
    }

    public String[] dictSigs() {
        return split(dictSigs);
    }

    public String[] titles() {
        return split(titles);
    }

    public String[] reqSigs() {
        return split(reqSigs);
    }

    //用于存储xml路径
    private String path;

    public XmlBean(String path) {
        this.path = path;
        XPathUtil.populateBeanFromXml(this);
    }

    public String getDictIndexes() {
        return dictIndexes;
    }

    public void setDictIndexes(String dictIndexes) {
        this.dictIndexes = dictIndexes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDictPath() {
        return dictPath;
    }

    public void setDictPath(String dictPath) {
        this.dictPath = dictPath;
    }

    public String getReqPath() {
        return reqPath;
    }

    public void setReqPath(String reqPath) {
        this.reqPath = reqPath;
    }

    public String getRespPath() {
        return respPath;
    }

    public void setRespPath(String respPath) {
        this.respPath = respPath;
    }

    public String getDictSigs() {
        return dictSigs;
    }

    public void setDictSigs(String dictSigs) {
        this.dictSigs = dictSigs;
    }

    public String getReqSigs() {
        return reqSigs;
    }

    public void setReqSigs(String reqSigs) {
        this.reqSigs = reqSigs;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(String commonNames) {
        this.commonNames = commonNames;
    }

    public String getCommonComments() {
        return commonComments;
    }

    public void setCommonComments(String commonComments) {
        this.commonComments = commonComments;
    }

    public String getCommonTypes() {
        return commonTypes;
    }

    public void setCommonTypes(String commonTypes) {
        this.commonTypes = commonTypes;
    }
}
