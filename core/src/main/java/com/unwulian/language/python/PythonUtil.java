package com.unwulian.language.python;

import cn.hutool.core.io.IoUtil;
import com.google.common.base.Joiner;

import java.io.BufferedReader;
import java.net.URL;

/**
 * python调用工具
 */
public class PythonUtil {
    private static final String PY_ENV = "D:\\software\\python\\python";

    public static String invoke(String pyEnv, String pyFile, String... params) {
        String content;
        try {
            String python = Joiner.on(" ")
                    .skipNulls()
                    .join(pyEnv, pyFile, params);

            Process pr = Runtime.getRuntime().exec(python);
            int i = pr.waitFor();
            if (i != 0) {
                throw new Exception("py run error");
            }
            BufferedReader reader = IoUtil.getReader(pr.getInputStream(), "GBK");
            content = IoUtil.read(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    public static String invokeDefault(String pyFile, String... params) {
        String invoke = invoke(PY_ENV, pyFile, params);
        return invoke;
    }

    public static void main(String[] args) {
        URL resource = PythonUtil.class.getClassLoader().getResource("py/extractTableFromPDF.py");
        String invoke = PythonUtil.invokeDefault(resource.getPath().substring(1), "C:\\Users\\Administrator\\Desktop\\test.pdf", "8","151");
        System.out.println(invoke);
    }
}
