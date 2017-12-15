package com.rizon.common.utils.excel;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Rizon on 2017/11/15.
 */
public class SimpleWorkbookFactory extends WorkbookFactory {

    public SimpleWorkbookFactory(List<?> headers, List<List<Object>> contents) {
        super();
        List<Header> newHeaders = createHeader(headers);
        super.setHeaders(newHeaders);
        super.setContents(createContent(newHeaders, contents));
    }

    private static List<Header> createHeader(List<?> headers) {
        return headers.stream().map(h -> {
            if (h instanceof Header) return (Header) h;
            else return new Header(String.valueOf(h));
        }).collect(Collectors.toList());
    }

    private static List<Map<String, String>> createContent(List<Header> headers, List<List<Object>> contents) {
        List<Map<String, String>> result = new ArrayList<>();
        for (List<Object> content : contents) {
            Map<String, String> row = new HashedMap<>();
            for (int i = 0; i < content.size(); i++) {
                row.put(headers.get(i).getKey(), getStringValue(content.get(i)));
            }
            result.add(row);
        }
        return result;
    }

    private static String getStringValue(Object value) {
        // todo 识别更多的数据类型
        return value == null ? StringUtils.EMPTY : String.valueOf(value);
    }
}
