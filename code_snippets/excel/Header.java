package com.rizon.common.utils.excel;

/**
 * Created by Rizon on 2017/11/15.
 */
public class Header {

    // key值
    private String key;
    // 名称
    private String value;
    // 列宽
    private Integer columnWidth;


    public Header(String value) {
        this.key = value;
        this.value = value;
    }


    public String getKey() {
        return key;
    }


    public String getValue() {
        return value;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public Header setKey(String key) {
        this.key = key;
        return this;
    }

    public Header setValue(String value) {
        this.value = value;
        return this;
    }

    public Header setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
        return this;
    }
}
