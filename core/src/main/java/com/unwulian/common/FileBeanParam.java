package com.unwulian.common;

/**
 * 输入的参数，提取的参数
 */
public class FileBeanParam {
    //输入起始页
    private int startPage;
    private int endPage;




    public FileBeanParam(int samePage) {
        this.startPage = samePage;
        this.endPage = samePage;
    }

    public FileBeanParam(int startPage, int endPage) {
        this.startPage = startPage;
        this.endPage = endPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }


}
