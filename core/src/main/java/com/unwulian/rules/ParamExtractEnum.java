package com.unwulian.rules;

public enum ParamExtractEnum {
    //pdf中的表格形式
    table("");
    //特征值
    private String characteristicValue;

    public ParamExtractEnum setCharacteristicValue(String characteristicValue) {
        this.characteristicValue = characteristicValue;
        return this;
    }

    ParamExtractEnum(String characteristicValue) {
        this.characteristicValue = characteristicValue;
    }
}
