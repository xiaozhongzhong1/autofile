package com.unwulian.doc.pdf;

import com.unwulian.common.Constants;
import com.unwulian.domain.PDFInterfacceField;
import com.unwulian.domain.PDFInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author: zzxu
 * @date: 2021/6/2 11:05
 * @description:
 */
public class PDFDataDeal {


    /****
     * 解析dealData
     * @param lines
     * @return
     */
    public static List<PDFInterface> dealData(String[] lines) {

        List<PDFInterface> list = new ArrayList<>();
        int flag = 0;
        PDFInterface anInterface = new PDFInterface();
        List<PDFInterfacceField> fieldList = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            //为空继续循环
            if (!Optional.ofNullable(lines[i]).isPresent()) {
                continue;
            }

            String line = lines[i].replace(Constants.LINE_FILTER_OPTIONAL, "");

            //获取接口名称和api地址
            if (flag == 0 && line.contains(Constants.INTERFACE_URI)) {
                if (line.contains(Constants.INTERFACE_URI)) {
                    String[] uri = line.split(Constants.BODY_SYMBOL_URI_SPLIT);
                    String[] name = uri[1].split("/");
                    anInterface.setUri(uri[1]);
                    anInterface.setOperator(name[name.length - 1]);
                    flag++;
                    continue;
                }
            }
            //是否是接口参数开始位置
            if (checkLineByBodyStart(flag, line)) {
                if (line.contains(Constants.BODY_SYMBOL_COLON) && line.contains("\"")) {
                    String[] fieldArr = line.replace("\"", "").split(Constants.BODY_SYMBOL_COLON);
                    PDFInterfacceField field = Constants.fieldMap.get(fieldArr[0]);
                    if (null != field) {
                        fieldList.add(field);
                    }
                }

            }

            //是否为接口参数结束位置
            if (checkLineByBodyEnd(flag, line) || i == lines.length - 1) {
                anInterface.setFieldList(fieldList);
                list.add(anInterface);
                flag = 0;
                anInterface = new PDFInterface();
                fieldList = new ArrayList();
            }

            //存入标题
            if (flag == 0 && line.matches(Constants.BODY_IS_MARTH) && line.contains(Constants.BODY_SYMBOL_POINT) && Pattern.matches(Constants.BODY_IS_CN, line) && !line.contains("：")) {
                String[] title = line.split("");
                anInterface.setTitle(getTitleStr(title));
            }


        }

        return list;
    }


    /****
     * 解析dealData
     * @param lines
     * @return
     */
    public static List<PDFInterface> dealDataShenzhen(String[] lines) {

        List<PDFInterface> list = new ArrayList<>();
        int flag = 0;
        PDFInterface anInterface = new PDFInterface();
        List<PDFInterfacceField> fieldList = new ArrayList<>();
        List<PDFInterfacceField> repFieldList = new ArrayList<>();
        String flagStr = "";

        for (int i = 0; i < lines.length; i++) {
            //为空继续循环
            if (!Optional.ofNullable(lines[i]).isPresent()) {
                continue;
            }

            //
            if (Constants.START_LINE.equals(lines[i]) || Constants.START_LINE_RES.equals(lines[i])) {
                flagStr = lines[i];
            }

            String line = lines[i].replace(" ", "");
            //获取接口名称和api地址
            if (flag == 0 && line.contains(Constants.INTERFACE_URI)) {
                String[] name = line.replace("\"", "").replace(",", "").split(Constants.BODY_SYMBOL_URI_SPLIT);
                anInterface.setUri(name[name.length - 1]);
                anInterface.setOperator(name[name.length - 1]);
                flag++;
            }

            //是否是接口参数开始位置
            if (checkLineByBodyStart(flag, line)) {
                if (line.contains(Constants.BODY_SYMBOL_COLON) && line.contains("\"")) {
                    String[] fieldArr = line.replace("\"", "").split(Constants.BODY_SYMBOL_COLON);
                    PDFInterfacceField field = Constants.fieldMap.get(fieldArr[0]);
                    if (Constants.START_LINE.equals(flagStr)) {
                        if (null != field) {
                            fieldList.add(field);
                        }
                    }
                    if (Constants.START_LINE_RES.equals(flagStr)) {
                        if (null != field) {
                            repFieldList.add(field);
                        }
                    }

                }
            }

            //是否为接口参数结束位置
            if (checkLineByBodyEnd(flag, line) || i == lines.length - 1) {
                anInterface.setFieldList(fieldList);
                anInterface.setResFieldList(repFieldList);
                list.add(anInterface);
                flagStr = "";
                flag = 0;
                anInterface = new PDFInterface();
                fieldList = new ArrayList();
                repFieldList = new ArrayList<>();
            }

            //存入标题
            if (flag == 0 && line.matches(Constants.BODY_IS_MARTH) && line.contains(Constants.BODY_SYMBOL_POINT) && Pattern.matches(Constants.BODY_IS_CN, line) && !line.contains("：")) {
                String[] title = line.split("");
                if (!line.contains("请求参数") && !line.contains("返回参数") && !line.contains("格式示例")) {
                    anInterface.setTitle(getTitleStr(title));
                }
            }


        }

        return list;
    }

    /***
     * 是否是接口参数开始位置判断
     * @param flag
     * @param line
     * @return
     */
    public static boolean checkLineByBodyStart(int flag, String line) {
        if ((flag == 0 && Constants.START_LINE.equals(line)) || flag > 0) {
            return true;
        }
        return false;
    }

    public static String getTitleStr(String[] str) {
        StringBuilder title = new StringBuilder();
        Arrays.stream(str).forEach(s -> {
            if (Pattern.matches(Constants.BODY_IS_CN_ALL, s) || Pattern.matches(Constants.BODY_IS_EN, s)) {
                title.append(s);
            }
        });
        return title.toString();
    }

    /****
     * 是否为接口参数结束位置判断
     * @param flag 标识
     * @param line 当前行的字符串
     */
    public static boolean checkLineByBodyEnd(int flag, String line) {
        if (flag > 0 && !line.contains(Constants.BODY_SYMBOL_COLON) && line.contains(Constants.BODY_SYMBOL_POINT) && line.matches(Constants.BODY_IS_MARTH)) {
            return true;
        }
        return false;
    }
}
