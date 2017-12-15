package com.rizon.common.utils.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Convenient superclass for Excel document views in the Office 2007 XLSX format
 * Created by Rizon on 2017/11/15.
 */
public class XlsxStreamingView extends AbstractXlsxStreamingView{
    private String fileName;
    private WorkbookFactory factory;
    public XlsxStreamingView(String fileName,WorkbookFactory factory) {
        this.fileName = fileName;
        this.factory = factory;
    }

    private void initFileName(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        if (request.getHeader("USER-AGENT") != null && (request.getHeader("USER-AGENT").contains("MSIE") || request.getHeader("USER-AGENT").contains("rv:11"))) {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xlsx", "utf-8"));
        } else {
            response.setHeader("Content-disposition", "attachment;filename=" + new String((fileName+".xlsx").getBytes("utf-8"), "iso-8859-1"));
        }
    }
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        initFileName(response, request);
        factory.writeWorkbook(workbook);
    }
}
