package com.unwulian.specification.codemodel;

public interface MdModel {
    String TITLE_PLACE_HOLDER = "{title}";
    String TYPE_PLACE_HOLDER = "{type}";
    String BODY_PLACE_HOLDER = "{body}";

    /**
     * 中鼎的model模型，目前也是通用模型
     */
    String ZD_MODEL = "### " + TITLE_PLACE_HOLDER + "\n"
            + "**type: " + TYPE_PLACE_HOLDER + "**\n"
            + "**payload:**\n"
            + "```\n"
            + BODY_PLACE_HOLDER + "\n"
            + "```";
}
