package com.unwulian.common;

import java.io.File;

public interface FileReader<T> {
    default T read(String filePath) {
        return read(new File(filePath));
    }

    T read(File file);
}
