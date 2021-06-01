package com.example.api.service.impl;

import cn.hutool.core.io.FileUtil;
import com.example.api.service.IFaceRecognitionService;
import com.unwulian.doc.pdf.PDFReader;
import com.unwulian.language.python.PythonUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Service
public class FaceRecognitionServiceImpl implements IFaceRecognitionService {
    private PDFReader pdfReader = new PDFReader();
    private final String DIR = "C:\\Users\\Administrator\\Desktop\\copy";
    private final String CONTENT_PATH =  DIR.concat("\\content.txt");
    private final String TABLES_PATH =  DIR.concat("\\tables.txt");
    /**
     * 通过规则去提取
     */
    @Override
    public void extract() {


        PDDocument doc = null;
        try {
            String file = "C:\\Users\\Administrator\\Desktop\\test.pdf";
            doc = pdfReader.read(file);
            String content = pdfReader.getContent(doc, 8, 151);
            FileUtil.writeString(content,new File(CONTENT_PATH),"UTF-8");
            System.out.println("content is ok");


            URL resource = PythonUtil.class.getClassLoader().getResource("py/extractTableFromPDF.py");
            String tables = PythonUtil.invokeDefault(resource.getPath().substring(1), "C:\\Users\\Administrator\\Desktop\\test.pdf"
                    , "8", "10");
            FileUtil.writeString(tables,new File(TABLES_PATH),"UTF-8");
            System.out.println("tables is ok");

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
}
