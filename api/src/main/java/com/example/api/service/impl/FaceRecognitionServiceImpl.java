package com.example.api.service.impl;

import com.example.api.service.IFaceRecognitionService;
import com.unwulian.doc.pdf.PDFReader;
import com.unwulian.language.python.PythonUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Service
public class FaceRecognitionServiceImpl implements IFaceRecognitionService {
    private PDFReader pdfReader = new PDFReader();

    /**
     * 通过规则去提取
     */
    @Override
    public void extract() {
        PDDocument doc = null;
        try {
            String file = "C:\\Users\\Administrator\\Desktop\\test.pdf";
            doc = pdfReader.read(file);
            String content = pdfReader.getContent(doc, 8, 9);
            URL resource = PythonUtil.class.getClassLoader().getResource("py/extractTableFromPDF.py");
            String tables = PythonUtil.invokeDefault(resource.getPath().substring(1), "C:\\Users\\Administrator\\Desktop\\test.pdf"
                    , "8", "9");

            System.out.println(tables);


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
