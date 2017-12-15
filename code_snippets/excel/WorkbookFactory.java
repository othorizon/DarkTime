package com.ksyun.bossv2.common.utils.excel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * Workbook写数据
 * Created by Rizon on 2017/11/15.
 */
public class WorkbookFactory {
    private Sheet sheet;//表格
    private CellStyle headerStyle;//标题头风格
    private CellStyle normalStyle;//常规风格
    private CellStyle redStyle;//重点标记风格
    private CellStyle greyStyle;//重点标记风格
    private CellStyle errorStyle;//重点标记风格

    // 数据
    // 列头
    private List<Header> headers;
    //内容 key值对应列的key
    private List<Map<String, String>> contents;

    // 配置
    private boolean freezeHeader = true;

    private void initDate(Workbook workbook) {
        sheet = workbook.createSheet();
        headerStyle = getHeaderStyle(workbook);
        headerStyle.setWrapText(true);

        normalStyle = getContentStyle(workbook, IndexedColors.BLACK, IndexedColors.WHITE);
        normalStyle.setWrapText(true);

        redStyle = getContentStyle(workbook, IndexedColors.RED, IndexedColors.WHITE);
        redStyle.setWrapText(true);

        greyStyle = getContentStyle(workbook, IndexedColors.BLACK, IndexedColors.GREY_25_PERCENT);
        greyStyle.setWrapText(true);

        errorStyle = getErrorStyle(workbook);
        greyStyle.setWrapText(true);
    }


    public WorkbookFactory() {
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public void setContents(List<Map<String, String>> contents) {
        this.contents = contents;
    }

    public static Workbook newSXSSFWorkbook() {
        return new SXSSFWorkbook();
    }
    /**
     * 写入workbook
     *
     * @return
     */
    public void writeWorkbook(Workbook workbook) {
        initDate(workbook);
        createHeader();
        if (CollectionUtils.isEmpty(contents)) {
            Row row = sheet.createRow(1);
            writeMessageRow("没有任何内容", row);
            return;
        }
        writeContent();
        return;
    }

    private void writeContent() {
        // 从第二行开始，第0行为标题
        int rowNum = 1;
        for (Map<String, String> content : contents) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.createCell(i);
                Header header = headers.get(i);
                // todo set from Content
                cell.setCellStyle(normalStyle);
                cell.setCellValue(content.getOrDefault(header.getKey(), StringUtils.EMPTY));
            }
        }
    }


    // 产生表格标题行
    private void createHeader() {
        if (freezeHeader) {
            sheet.createFreezePane(0, 1);
        }
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Header header = headers.get(i);
            Cell cell = row.createCell(i);
            // todo set from Header
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header.getValue());
            if (header.getColumnWidth() != null) {
                sheet.setColumnWidth(i, header.getColumnWidth());
            } else {
                sheet.autoSizeColumn(i,true);
            }
        }
    }


    private void writeMessageRow(String message, Row row) {
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(),
                0, 4));
        Cell cellHead = row.createCell(0);
        cellHead.setCellStyle(errorStyle);
        cellHead.setCellValue(message);
    }

    //设置表头格式
    public static CellStyle getHeaderStyle(Workbook wb) {
        // 表头字体
        Font f = wb.createFont();
        f.setFontHeightInPoints((short) 12);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        f.setFontName("幼圆");
        //表头单元格样式
        CellStyle cs = wb.createCellStyle();
        cs.setFont(f);
        cs.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs.setLeftBorderColor(HSSFColor.GREY_50_PERCENT.index);
        cs.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
        cs.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);
        cs.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
        return cs;
    }
    //设置内容格式
    public static CellStyle getContentStyle(Workbook wb, IndexedColors fontColor, IndexedColors bgColor) {
        //内容字体
        Font f2 = wb.createFont();
        f2.setFontHeightInPoints((short) 11);
        f2.setColor(fontColor.getIndex());
        f2.setFontName("幼圆");
        //内容单元格样式
        CellStyle cs2 = wb.createCellStyle();
        cs2.setFont(f2);
        cs2.setFillForegroundColor(bgColor.getIndex());
        cs2.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setLeftBorderColor(HSSFColor.GREY_50_PERCENT.index);
        cs2.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
        cs2.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);
        cs2.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
        cs2.setAlignment(CellStyle.ALIGN_LEFT);
        cs2.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        return cs2;
    }
    //设置错误码样式
    public static CellStyle getErrorStyle(Workbook wb) {
        //标题字体
        Font h = wb.createFont();
        h.setFontHeightInPoints((short) 11);
        h.setColor(IndexedColors.RED.getIndex());
        h.setFontName("幼圆");
        h.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //标题单元格样式
        CellStyle csHead = wb.createCellStyle();
        csHead.setAlignment(CellStyle.ALIGN_CENTER);
        csHead.setFont(h);
        return csHead;
    }
}
