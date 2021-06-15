package com.example.api.service.impl;

import cn.hutool.core.io.FileUtil;
import com.example.api.service.IFaceRecognitionService;
import com.google.common.collect.Lists;
import com.unwulian.doc.pdf.PDFReader;
import com.unwulian.rules.FaceRecognitionRule;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FaceRecognitionServiceImpl implements IFaceRecognitionService {
    private PDFReader pdfReader = new PDFReader();
    private final String DIR = "C:\\Users\\Administrator\\Desktop\\copy";
    private final String CONTENT_PATH = DIR.concat("\\content.txt");
    private final String TABLES_PATH = DIR.concat("\\tables.txt");

    /**
     * 通过规则去提取
     */
    @Override
    public void extract() {


        PDDocument doc = null;
        try {
            String file = "C:\\Users\\Administrator\\Desktop\\test.pdf";
            doc = pdfReader.read(file);
            String content = pdfReader.getContent(doc, 7, 151);
            FileUtil.writeString(content, new File(CONTENT_PATH), "UTF-8");
            System.out.println("content is ok");


        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Objects.requireNonNull(doc);
            try {
                doc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分析数据
     */
    @Override
    public void analyze() {
        parseUrl();
    }

    private void parseUrl() {
        File file = new File(CONTENT_PATH);
        String content = FileUtil.readString(file, "utf-8");
        FaceRecognitionRule faceRecognitionRule = new FaceRecognitionRule();
        Pattern pattern = Pattern.compile(faceRecognitionRule.getUrlExtractEnum().getRegex());
        List<String> urls = Lists.newArrayList();
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String group = matcher.group();
            urls.add(group);
        }

        System.out.println(urls.stream().collect(Collectors.joining(System.lineSeparator())));

    }


    public static void main(String[] args) {
        new FaceRecognitionServiceImpl().parseUrl();
    }
}
