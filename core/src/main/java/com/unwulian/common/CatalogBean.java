package com.unwulian.common;

import java.util.List;

/**
 * 目录对象：包含文档名和章节对象
 */
public class CatalogBean {
    private String docName;
    private int totalPages;
    private List<Chapter> chapters;

    public CatalogBean(String docName, int totalPages, List<Chapter> chapters) {
        this.docName = docName;
        this.totalPages = totalPages;
        this.chapters = chapters;
    }

    public CatalogBean() {
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

}
