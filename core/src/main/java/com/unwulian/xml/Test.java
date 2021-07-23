package com.unwulian.xml;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        test.test();
    }

    public void test(){
        String path = "C:\\Users\\Administrator\\Desktop\\E7.doc";
        try(FileInputStream in = new FileInputStream(path)){
            POIFSFileSystem pfs = new POIFSFileSystem(in);
            HWPFDocument hwpf = new HWPFDocument(pfs);
            Range range = hwpf.getRange();// 得到文档的读取范围
            TableIterator it = new TableIterator(range);
            Gson gson = new Gson();
            List<List<List<String>>> tables = new ArrayList<>();
            JSONArray request = new JSONArray();
            JSONArray response = new JSONArray();

            StringBuilder reqStr = new StringBuilder();
            StringBuilder apiName = new StringBuilder();
            StringBuilder apiUrl = new StringBuilder();
            StringBuilder apiMethod = new StringBuilder();
            StringBuilder error = new StringBuilder();
            StringBuilder titleIsNull = new StringBuilder();
            String apiNameTemp = "";
            while (it.hasNext()) {
                List<List<String>> rows = new ArrayList<>();
                Table tb = (Table) it.next();
                outer:
                for (int i = 0; i < tb.numRows(); i++) {
                    TableRow tr = tb.getRow(i);
                    List<String> cols = new ArrayList<>();
                    // 迭代列，默认从0开始
                    for (int j = 0; j < tr.numCells(); j++) {
                        TableCell td = tr.getCell(j);// 取得单元格
                        // 取得单元格的内容
                        for (int k = 0; k < td.numParagraphs(); k++) {
                            Paragraph para = td.getParagraph(k);
                            String s = para.text();
                            // 去除后面的特殊符号
                            if (null != s && !"".equals(s)) {
                                s = s.substring(0, s.length() - 1);
                            }
                            s = s.trim();

                            if(i == tb.numRows() - 1){
                                if(s.contains("传入格式")){
                                    Paragraph next = td.getParagraph(++k);
                                    request.add(dealJson(next.text()));
                                }
                                if(s.contains("结果示例")){
                                    Paragraph next = td.getParagraph(++k);
                                    response.add(dealJson(next.text()));
                                }
                            }

                            //apiUrl
                            if(i == 0 && j == 1){
                                String api = s.replace("/","");
                                apiNameTemp = s;
                                apiUrl.append(api+",");
                                apiMethod.append(api+"Request,"+api+"Response,");
                            }

                            //没有应用场景
                            if(i == 2 && j == 0){
                                if(!s.equals("应用场景")){
                                    titleIsNull.append(apiNameTemp+",");
                                }
                            }

                            //apiName
                            if(i == 2 && j == 1){
                                String apiNameStr = s.replace(",","").replace("，","");
                                apiName.append(apiNameStr+",");
                            }

                            //遇到返回结果就插入新的
                            if(tr.numCells() == 1 && s.equals("返回结果")){
                                tables.add(rows);
                                rows = new ArrayList<>();
                                continue;
                            }

                            //标识有问题的接口
                            if(i == 4 && tr.numCells() < 4){
                                error.append(apiNameTemp+",");
                            }
                            if(tr.numCells() == 4){
                                cols.add(s);
                            }

                        }
                    }
                    if(tr.numCells() == 4){
                        rows.add(cols);
                    }
                }
                tables.add(rows);
            }
            JsonArray array = gson.toJsonTree(tables).getAsJsonArray();
            System.out.println(array);
            System.out.println("_______________apiName_________________");
            System.out.println(apiName.toString());
            System.out.println("________________apiUrl________________");
            System.out.println(apiUrl.toString());
            System.out.println("________________apiMethod________________");
            System.out.println(apiMethod.toString());
            System.out.println("________________error________________");
            System.out.println(error.toString());
            System.out.println("________________titleIsNull________________");
            System.out.println(titleIsNull.toString());
            System.out.println("________________RequestJson________________");
            System.out.println(request);
            System.out.println("________________ResponseJson________________");
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public JSONObject dealJson(String next){
        String str = next.replace("\u000B    ","").replace("\u000B","");
        try{
            JSONObject jsonObject = new JSONObject(str);
            return jsonObject;
        }catch (Exception e){
            System.out.println(next);
            return null;
        }
    }
}
