package com.unwulian.specification.bean;

import com.google.gson.JsonArray;
import com.unwulian.specification.utils.RegexUtil;

public class TableBean {
    private String name;
    private String comment;
    private String type;

    public TableBean(JsonArray arr, TableMap tableMap) {
        Integer nameIndex = tableMap.getNameIndex();
        Integer commentIndex = tableMap.getCommentIndex();
        Integer typeIndex = tableMap.getTypeIndex();
        this.name = arr.get(nameIndex).getAsString();
        this.comment = RegexUtil.removeSpecialChar(arr.get(commentIndex).getAsString());
        try {
            this.type = RegexUtil.formatType(arr.get(typeIndex).getAsString());
        }catch (Exception e){
            System.out.println(arr.toString()+e);

        }
    }


    public TableBean(String name, String comment, String type) {
        this.name = name;
        this.comment = comment;
        this.type = type;
    }

    public TableBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
