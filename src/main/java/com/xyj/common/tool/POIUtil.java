package com.xyj.common.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @className:POIUtil.java
 * @classDescription:POI操作类
 * @author:xiayingjie
 * @createTime:2010-10-29
 */

public class POIUtil {
    // 读取行的分隔符
    public static String separator = " - ";

    // ------------------------写Excel-----------------------------------

    /**
     * 创建workBook对象 xlsx(2007以上版本)
     *
     * @return
     */
    public static Workbook createWorkbook() {
        return createWorkbook(true);
    }

    /**
     * 创建WorkBook对象
     *
     * @param flag true:xlsx(1997-2007) false:xls(2007以上)
     * @return
     */
    public static Workbook createWorkbook(boolean flag) {
        Workbook wb;
        if (flag) {
            wb = new XSSFWorkbook();
        } else {
            wb = new HSSFWorkbook();
        }
        return wb;
    }

    /**
     * 添加图片
     *
     * @param wb          workBook对象
     * @param sheet       sheet对象
     * @param picFileName 图片文件名称（全路径）
     * @param picType     图片类型
     * @param row         图片所在的行
     * @param col         图片所在的列
     */
    public static void addPicture(Workbook wb, Sheet sheet, String picFileName,
                                  int picType, int row, int col) {
        InputStream is = null;
        try {
            // 读取图片
            is = new FileInputStream(picFileName);
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = wb.addPicture(bytes, picType);
            is.close();
            // 写图片
            CreationHelper helper = wb.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            // 设置图片的位置
            anchor.setCol1(col);
            anchor.setRow1(row);
            Picture pict = drawing.createPicture(anchor, pictureIdx);

            pict.resize();
        } catch (Exception e) {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * 创建Cell 默认为水平和垂直方式都是居中
     *
     * @param style  CellStyle对象
     * @param row    Row对象
     * @param column 单元格所在的列
     * @return
     */
    public static Cell createCell(CellStyle style, Row row, short column) {
        return createCell(style, row, column, XSSFCellStyle.ALIGN_CENTER,
                XSSFCellStyle.ALIGN_CENTER);
    }

    /**
     * 创建Cell并设置水平和垂直方式
     *
     * @param style  CellStyle对象
     * @param row    Row对象
     * @param column 单元格所在的列
     * @param halign 水平对齐方式：XSSFCellStyle.VERTICAL_CENTER.
     * @param valign 垂直对齐方式：XSSFCellStyle.ALIGN_LEFT
     */
    public static Cell createCell(CellStyle style, Row row, short column,
                                  short halign, short valign) {
        Cell cell = row.createCell(column);
        setAlign(style, halign, valign);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 合并单元格
     *
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow  最后行
     * @param firstCol 开始列
     * @param lastCol  最后列
     */
    public static void mergeCell(Sheet sheet, int firstRow, int lastRow,
                                 int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol,
                lastCol));
    }

    // ---------------------------------设置样式-----------------------

    /**
     * 设置单元格对齐方式
     *
     * @param style
     * @param halign
     * @param valign
     * @return
     */
    public static CellStyle setAlign(CellStyle style, short halign, short valign) {
        style.setAlignment(halign);
        style.setVerticalAlignment(valign);
        return style;
    }

    /**
     * 设置单元格边框(四个方向的颜色一样)
     *
     * @param style       style对象
     * @param borderStyle 边框类型 ：dished-虚线 thick-加粗 double-双重 dotted-有点的
     *                    CellStyle.BORDER_THICK
     * @param borderColor 颜色 IndexedColors.GREEN.getIndex()
     * @return
     */
    public static CellStyle setBorder(CellStyle style, short borderStyle,
                                      short borderColor) {

        // 设置底部格式（样式+颜色）
        style.setBorderBottom(borderStyle);
        style.setBottomBorderColor(borderColor);
        // 设置左边格式
        style.setBorderLeft(borderStyle);
        style.setLeftBorderColor(borderColor);
        // 设置右边格式
        style.setBorderRight(borderStyle);
        style.setRightBorderColor(borderColor);
        // 设置顶部格式
        style.setBorderTop(borderStyle);
        style.setTopBorderColor(borderColor);

        return style;
    }

    /**
     * 设置前景颜色
     *
     * @param style style对象
     * @param color ：IndexedColors.YELLOW.getIndex()
     * @return
     */
    public static CellStyle setBackColor(CellStyle style, short color) {

        // 设置前端颜色
        style.setFillForegroundColor(color);
        // 设置填充模式
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        return style;
    }

    /**
     * 设置背景颜色
     *
     * @param style       style对象
     * @param backColor   ：IndexedColors.YELLOW.getIndex()
     * @param fillPattern ：CellStyle.SPARSE_DOTS
     * @return
     */
    public static CellStyle setBackColor(CellStyle style, short backColor,
                                         short fillPattern) {

        // 设置背景颜色
        style.setFillBackgroundColor(backColor);

        // 设置填充模式
        style.setFillPattern(fillPattern);

        return style;
    }

    /**
     * 设置字体（简单的需求实现，如果复杂的字体，需要自己去实现）尽量重用
     *
     * @param style    style对象
     * @param fontSize 字体大小 shot(24)
     * @param color    字体颜色 IndexedColors.YELLOW.getIndex()
     * @param fontName 字体名称 "Courier New"
     * @param
     */
    public static CellStyle setFont(Font font, CellStyle style, short fontSize,
                                    short color, String fontName) {
        font.setFontHeightInPoints(color);
        font.setFontName(fontName);

        // font.setItalic(true);// 斜体
        // font.setStrikeout(true);//加干扰线

        font.setColor(color);// 设置颜色
        // Fonts are set into a style so create a new one to use.
        style.setFont(font);

        return style;

    }

    /**
     * @param createHelper createHelper对象
     * @param style        CellStyle对象
     * @param formartData  date:"m/d/yy h:mm";
     */
    public static CellStyle setDataFormat(CreationHelper createHelper,
                                          CellStyle style, String formartData) {

        style.setDataFormat(createHelper.createDataFormat().getFormat(
                formartData));

        return style;
    }

    /**
     * 将Workbook写入文件
     *
     * @param wb       workbook对象
     * @param fileName 文件的全路径
     * @return
     */
    public static boolean createExcel(Workbook wb, String fileName) {
        boolean flag = true;
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            flag = false;
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        return flag;
    }

    // -----------------------读excel----------------

    /**
     * 读取文件
     *
     * @param path
     * @return
     */
    public static Workbook createWorkbook(String path) {
        Workbook wb = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File(path));

            if (path.trim().endsWith(".xlsx")) {
                wb = new XSSFWorkbook(is);
            } else if (path.trim().endsWith(".xls")) {
                wb = new HSSFWorkbook(is);

            } else {
                throw new IllegalArgumentException("文件是非excel文件，请选择正确的文件");
            }
        } catch (Exception e) {
            try {
                is.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 查找行
     *
     * @param sheet
     * @param row
     * @return
     */
    public static String getLine(Sheet sheet, int row) {

        // 根据行数取得Sheet的一行

        Row rowline = sheet.getRow(row);

        // 创建字符创缓冲区

        StringBuffer buffer = new StringBuffer();

        // 获取当前行的列数

        int filledColumns = rowline.getLastCellNum();

        Cell cell = null;

        // 循环遍历所有列

        for (int i = 0; i < filledColumns; i++) {

            // 取得当前Cell

            cell = rowline.getCell((short) i);

            String cellvalue = null;

            if (cell != null) {

                // 判断当前Cell的Type

                switch (cell.getCellType()) {

                    // 如果当前Cell的Type为NUMERIC

                    case Cell.CELL_TYPE_NUMERIC: {

                        // 判断当前的cell是否为Date

                        if (org.apache.poi.ss.usermodel.DateUtil
                                .isCellDateFormatted(cell)) {

                            // 如果是Date类型则，取得该Cell的Date值

                            Date date = cell.getDateCellValue();

                            // 把Date转换成本地格式的字符串

                            cellvalue = cell.getDateCellValue().toLocaleString();

                        }

                        // 如果是纯数字

                        else {

                            // 取得当前Cell的数值

                            Integer num = new Integer((int) cell

                                    .getNumericCellValue());

                            cellvalue = String.valueOf(num);

                        }

                        break;

                    }

                    // 如果当前Cell的Type为STRIN

                    case Cell.CELL_TYPE_STRING:

                        // 取得当前的Cell字符串

                        cellvalue = cell.getStringCellValue().replaceAll("'", "''");

                        break;

                    // 默认的Cell值

                    default:

                        cellvalue = " ";

                }

            } else {

                cellvalue = "";

            }

            // 在每个字段之间插入分割符

            buffer.append(cellvalue).append(separator);

        }

        // 以字符串返回该行的数据

        return buffer.toString();

    }

    public static void showDaoruShuju() {
        // TODO Auto-generated method stub
        // --------读-----------------
        Workbook wb = createWorkbook("F://se1.xls");
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            for (int j = 0; j < sheet.getLastRowNum(); j++) {
                System.out.println(getLine(sheet, j));
            }
        }

    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // ----------读---------------
        //showDaoruShuju();
        // // -------------插入---------------------
        // Workbook wb = POIUtil.createWorkbook();
        // //创建Sheet
        // Sheet s = wb.createSheet();
        // //创建样式（真实项目中-所有样式都写在这里）
        // CellStyle style1=wb.createCellStyle();
        // CellStyle style2=wb.createCellStyle();
        // CellStyle style3=wb.createCellStyle();
        // //字体
        // //设置字体
        // Font font=wb.createFont();
        // CellStyle fontStyle
        // =setFont(font,wb.createCellStyle(),(short)30,IndexedColors.RED.getIndex()
        // , "Courier New");
        // //合并单元格
        // //mergeCell(s,2,2,1,2);
        // //创建行
        // Row row1 = s.createRow(0);
        // //-----------数字-----------
        // Cell c1=createCell(style1, row1, (short) 0);
        // c1.setCellValue(3.138);
        // //设置边框
        // setBorder(style1,CellStyle.BORDER_THIN,IndexedColors.GREEN.getIndex());
        //
        //
        // //-------------日期----------------
        // Cell c2=createCell(style2, row1, (short) 1);
        // c2.setCellValue(Calendar.getInstance());
        // CreationHelper ch=wb.getCreationHelper();
        // setDataFormat(ch,style2,"m/d/yy h:mm");
        // setBackColor(style2,IndexedColors.YELLOW.getIndex());
        //
        //
        //
        // Cell c4=createCell(style2, row1, (short) 2);
        //
        // //----------------字符串------------------
        //
        // //Cell c3=createCell(style3, row1, (short) 2);
        // Cell c3=row1.createCell((short) 3);
        // c3.setCellValue("我和你");
        // CellStyle cs=wb.createCellStyle();
        //
        // setBackColor(cs,IndexedColors.ORANGE.getIndex());
        //
        // c3.setCellStyle(cs);
        // c3.setCellStyle(fontStyle);
        //
        // //写入图片
        // // POIUtil.addPicture(wb, s,"F://aa.gif",
        // // Workbook.PICTURE_TYPE_JPEG,5,6);
        // POIUtil.createExcel(wb, "F://text.xlsx");
        Workbook wb = POIUtil.createWorkbook();
        // 创建sheet
        Sheet s = wb.createSheet();
        // 创建行
        Row row1 = s.createRow(0);

        CellStyle style1 = wb.createCellStyle();

        Cell c1 = createCell(style1, row1, (short) 0);
        c1.setCellValue("第一行第一 列");

        Cell c2 = createCell(style1, row1, (short) 1);
        c2.setCellValue("第一行第二 列");

        mergeCell(s,1,2 ,1,2);

    POIUtil.createExcel(wb, "/Users/xiayingjie/Downloads/text.xlsx");

    }

}
