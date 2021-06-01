package com.example.api.service.impl;

import com.example.api.service.IFaceRecognitionService;
import com.unwulian.doc.pdf.PDFReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
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
            String content = pdfReader.getContent(doc, 8, 151);



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
