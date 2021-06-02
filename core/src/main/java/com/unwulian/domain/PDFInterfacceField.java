package com.unwulian.domain;

import java.io.Serializable;

/**
 * @author: zzxu
 * @date: 2021/6/2 11:22
 * @description:
 */
public class PDFInterfacceField implements Serializable {

    private String name;

    private String remark;

    private String type;

    private String range;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "PDFInterfacceField{" +
                "name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", type='" + type + '\'' +
                ", range='" + range + '\'' +
                '}';
    }
}
