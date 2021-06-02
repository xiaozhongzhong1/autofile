package com.unwulian.doc.pdf;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONArray;
import com.google.gson.Gson;
import com.unwulian.common.Constants;
import com.unwulian.domain.PDFInterfacceField;
import com.unwulian.domain.PDFInterface;

import java.util.*;

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
    public static List<PDFInterface> dealData(String [] lines){

        List<PDFInterface> list = new ArrayList<>();
        int flag = 0;
        PDFInterface anInterface = new PDFInterface();
        List<PDFInterfacceField> fieldList = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            //为空继续循环
            if(!Optional.ofNullable(lines[i]).isPresent()){
                continue;
            }
            //获取接口名称和api地址
            if(flag == 0 && lines[i].contains(Constants.INTERFACE_URI)){
                if(lines[i].contains(Constants.INTERFACE_URI)){
                    String [] uri = lines[i].split(Constants.BODY_SYMBOL_URI_SPLIT);
                    String [] name = uri[1].split("/");
                    anInterface.setUri(uri[1]);
                    anInterface.setOperator(name[name.length-1]);
                    anInterface.setTitle(name[name.length-1]);
                    flag++;
                    continue;
                }
            }
            //是否是接口参数开始位置
            if(checkLineByBodyStart(flag,lines[i])){
                if(lines[i].contains(Constants.BODY_SYMBOL_COLON) && lines[i].contains("\"")){
                    String [] fieldArr = lines[i].replace("\"","").split(Constants.BODY_SYMBOL_COLON);
                    PDFInterfacceField field = Constants.fieldMap.get(fieldArr[0]);
                    if(null != field){
                        fieldList.add(field);
                    }
                }

            }

            //是否为接口参数结束位置
            if(checkLineByBodyEnd(flag,lines[i]) || i == lines.length-1){
                anInterface.setFieldList(fieldList);
                list.add(anInterface);
                flag = 0;
                anInterface = new PDFInterface();
                fieldList = new ArrayList();
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
    public static boolean checkLineByBodyStart(int flag,String line){
        if((flag == 0 && Constants.START_LINE.equals("line")) || flag > 0){
            return true;
        }
        return false;
    }



    /****
     * 是否为接口参数结束位置判断
     * @param flag 标识
     * @param line 当前行的字符串
     */
    public static boolean checkLineByBodyEnd(int flag,String line){
        if(flag > 0 && !line.contains(Constants.BODY_SYMBOL_COLON) && line.contains(Constants.BODY_SYMBOL_POINT) && line.matches(Constants.BODY_IS_MARTH)){
            return true;
        }
        return false;
    }
}
