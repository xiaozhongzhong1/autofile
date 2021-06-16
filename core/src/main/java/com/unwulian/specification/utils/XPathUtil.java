package com.unwulian.specification.utils;

import com.unwulian.specification.anno.XpathNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * 使用xpath解析document
 */
public class XPathUtil {

    private static XPath getXpath() {
        try {
            // 创建XPath对象
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            return xPath;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private static Document getDocument(String path) {
        try {
            // 创建Document对象
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            //两种加载资源文件的方法
            InputStream resourceAsStream = XPathUtil.class.getClassLoader().getResourceAsStream(path);
            Document parse = db.parse(resourceAsStream);
            return parse;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static <T> void populateBeanFromXml(T t) {
        Class<?> tClass = t.getClass();
        String path = null;
        try {
            Field field = tClass.getDeclaredField("path");
            field.setAccessible(true);
            path = (String)field.get(t);
        } catch (Exception e) {
            throw new RuntimeException("无法获取path字段");
        }

        XPath xpath = getXpath();
        Document doc = getDocument(path);
        for (Field declaredField : tClass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            XpathNode annotation = declaredField.getAnnotation(XpathNode.class);
            if (annotation != null) {
                String node = annotation.value();
                try {
                    Node node1 = (Node) xpath.evaluate(node, doc,
                            XPathConstants.NODE);
                    String textContent = node1.getTextContent();
                    declaredField.set(t, textContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
