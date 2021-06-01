package com.unwulian.rules;

public class FaceRecognitionRule implements IRule {
    /**
     * 表格第一行的特征值
     */
    private final String character = "key-Type-Values-Description";

    /**
     * url的regex
     */
    private final String regex = "URI:http://<server ipaddr>/action/(.*)?";

    private ParamExtractEnum paramExtract = ParamExtractEnum.table;
    private URLExtractEnum urlExtractEnum = URLExtractEnum.REGEX;

    {
        paramExtract.setCharacteristicValue(character);
        urlExtractEnum.setRegex(regex);
    }


    public ParamExtractEnum getParamExtract() {
        return paramExtract;
    }
    
    public URLExtractEnum getUrlExtractEnum() {
        return urlExtractEnum;
    }


}
