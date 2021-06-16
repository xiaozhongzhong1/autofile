package com.unwulian.specification.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static final Pattern INT_PATTERN;
    private static final Pattern STR_PATTERN;
    private static final Pattern OBJ_PATTERN;
    private static final Pattern ARR_PATTERN;

    private static final String INT_REGEX = "整型|int";
    private static final String STR_REGEX = "字符串|string";
    private static final String OBJ_REGEX = "json对象";
    private static final String ARR_REGEX = "array";
    static {
        INT_PATTERN = Pattern.compile(getIgnoreBlankPattern(INT_REGEX));
        STR_PATTERN = Pattern.compile(getIgnoreBlankPattern(STR_REGEX));
        OBJ_PATTERN = Pattern.compile(getIgnoreBlankPattern(OBJ_REGEX));
        ARR_PATTERN = Pattern.compile(getIgnoreBlankPattern(ARR_REGEX));
    }


    public static String formatType(String unformated) {
        unformated = unformated.trim().toLowerCase();

        Matcher matcher = INT_PATTERN.matcher(unformated);
        if (matcher.find()) {
            return "int";
        }
        Matcher strMatcher = STR_PATTERN.matcher(unformated);
        if (strMatcher.find()) {
            return "string";
        }
        Matcher objMatcher = OBJ_PATTERN.matcher(unformated);
        if (objMatcher.find()) {
            return "struct";
        }
        Matcher arrMatcher = ARR_PATTERN.matcher(unformated);
        if (arrMatcher.find()) {
            return "array";
        }
        throw new RuntimeException("do not find match type");
    }


    private static String getIgnoreBlankPattern(String regex) {
        StringBuilder strBuilder = new StringBuilder();
        for (char strChar : regex.toLowerCase().toCharArray()) {
            strBuilder.append(strChar).append("(\\s)?");
        }
        return strBuilder.toString().trim();
    }

    public static String removeSpecialChar(String ori){
        String s = ori.replaceAll("\\n", "");
        String s1 = s.replaceAll("\\t", "");
        String s2 = s.replaceAll("\"", "");
        return s2;
    }
}
