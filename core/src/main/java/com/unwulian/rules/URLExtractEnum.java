package com.unwulian.rules;

public enum URLExtractEnum {
    REGEX("");
    private String regex;

    public String getRegex() {
        return regex;
    }

    public URLExtractEnum setRegex(String regex) {
        this.regex = regex;
        return this;
    }

    URLExtractEnum(String regex) {
        this.regex = regex;
    }
}
