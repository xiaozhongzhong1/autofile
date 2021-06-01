package com.unwulian.language.python;

import cn.hutool.core.io.IoUtil;
import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

/**
 * python调用工具
 */
public class PythonUtil {

    public static String invoke(String pyFile, String... params) {
        String content;
        try {
            String python = Joiner.on(" ")
                    .skipNulls()
                    .join("python", pyFile, params);

            Process pr = Runtime.getRuntime().exec(python);
            BufferedReader reader = IoUtil.getReader(pr.getInputStream(), "utf-8");
            content = IoUtil.read(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static void main(String[] args) {
        URL resource = PythonUtil.class.getClassLoader().getResource("py/extractTableFromPDF.py");
        String invoke = PythonUtil.invoke(resource.getFile(), "C:\\Users\\Administrator\\Desktop\\test.pdf", "150");
        System.out.println(invoke);
    }
}
