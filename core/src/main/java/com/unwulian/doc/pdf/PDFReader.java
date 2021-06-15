package com.unwulian.doc.pdf;

import cn.hutool.core.io.file.FileWriter;
import com.unwulian.common.CatalogBean;
import com.unwulian.common.FileReader;
import com.unwulian.domain.PDFInterfacceField;
import com.unwulian.domain.PDFInterface;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    /**
     * 获取所有页数的内容，便于用正则获取url信息
     *
     * @param doc
     * @param startPage
     * @param endPage
     * @return
     */
    public String getContent(PDDocument doc, int startPage, int endPage) {
        PDFTextStripper textStripper;
        try {
            textStripper = new PDFTextStripper("GBK");
            textStripper.setSortByPosition(true);
            textStripper.setStartPage(startPage);
            textStripper.setEndPage(endPage);
            String content = textStripper.getText(doc);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取" + startPage + "-" + endPage + "的内容失败");
        }

    }

    public CatalogBean getCatalogBean(PDDocument doc) throws IOException {
        PDFCatalogParser pdfCatalogParser = new PDFCatalogParser(doc);
        CatalogBean catalogBean = pdfCatalogParser.getCatalogBean();
        return catalogBean;
    }

    /**
     * 将获取到的内容输出到markdown文档中去
     * @param list
     */
    public static void writeMD(List<PDFInterface> list) {
        FileWriter writer = new FileWriter("f:\\深圳人脸识别.md");
        list.stream().forEach(l -> {
            StringBuilder start = new StringBuilder("### ")
                    .append(l.getTitle()).append("\n")
                    .append("**type: ")
                    .append(l.getOperator()).append("**").append("\n")
                    .append("**payload:**").append("\n")
                    .append("```").append("\n").append("request").append("\n");
            List<PDFInterfacceField> fieldList = l.getFieldList();
            fieldList.stream().forEach(f -> start.append(f.getName())
                    .append(":").append(f.getType())
                    .append(":").append(f.getRemark().replace(":", "."))
                    .append("\n"));
            start.append("response").append("\n");
            List<PDFInterfacceField> repFieldList = l.getResFieldList();
            repFieldList.stream().forEach(f -> start.append(f.getName())
                    .append(":").append(f.getType())
                    .append(":").append(f.getRemark().replace(":", "."))
                    .append("\n"));
            start.append("```").append("\n");
            writer.append(start.toString());
        });

    }

    public static void main(String[] args) throws IOException {
        PDDocument doc = null;
        try {
            PDFReader pdfReader = new PDFReader();
            String filePath = "C:\\Users\\Administrator\\Desktop\\人脸识别.pdf";
            doc = pdfReader.read(filePath);
            CatalogBean catalogBean = pdfReader.getCatalogBean(doc);
            System.out.println(catalogBean);
            /*String content = pdfReader.getContent(doc, 4, 128);
            String[] lines = content.split("\r\n");
            List<PDFInterface> list = PDFDataDeal.dealDataShenzhen(lines);
            writeMD(list);*/
        } finally {
            doc.close();
        }
    }


}
