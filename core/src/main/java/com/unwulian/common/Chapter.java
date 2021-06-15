package com.unwulian.common;

import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 章节对象
 * 包含标题和页数
 */
public class Chapter {
    private String title;
    private int page;
    private PDOutlineItem pdOutlineItem;
    private List<Chapter> children;
    private String content;
    public Chapter() {
    }

    public Chapter(String title, int page, PDOutlineItem pdOutlineItem, List<Chapter> children) {
        this.title = title;
        this.page = page;
        this.pdOutlineItem = pdOutlineItem;
        this.children = children;
    }

    public PDOutlineItem getPdOutlineItem() {
        return pdOutlineItem;
    }

    public void setPdOutlineItem(PDOutlineItem pdOutlineItem) {
        this.pdOutlineItem = pdOutlineItem;
    }

    public List<Chapter> getChildren() {
        return children;
    }

    public void setChildren(List<Chapter> children) {
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getContent() {
        return content;
    }

    public void setContent(PDDocument doc) {
        PDPage destinationPage = null;
        try {
            destinationPage = pdOutlineItem.findDestinationPage(doc);
            String content = destinationPage.getContents().getInputStreamAsString();
            this.content = content;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
