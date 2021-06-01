package com.unwulian.doc.pdf;

import com.unwulian.common.FileReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PDFReader implements FileReader<PDDocument> {

    @Override
    public PDDocument read(File file) {
        PDDocument doc = null;
        try {
            //加载一个pdf对象
            doc = PDDocument.load(file);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return doc;
    }


    public static void main(String[] args) throws IOException {
        PDDocument read = null;
        try {
            PDFReader pdfReader = new PDFReader();
            String file = "C:\\Users\\Administrator\\Desktop\\test.pdf";
            read = pdfReader.read(new File(file));




            PDFTextStripper textStripper = new PDFTextStripper("GBK");
            textStripper.setSortByPosition(true);
            textStripper.setStartPage(7);
            textStripper.setEndPage(151);
            String content = textStripper.getText(read);
            System.out.println(read);
        } finally {
            read.close();
        }
    }
}
