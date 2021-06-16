package com.unwulian.specification.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
@Documented
public @interface XpathNode {
    String value();
}
