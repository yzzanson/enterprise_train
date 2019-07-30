package com.enterprise.util.excel;

import com.alibaba.fastjson.JSONObject;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/16 上午11:24
 */
public class MultiExcelUtil {
    /**
     * Excel文档对象
     */
    private HSSFWorkbook wb;
    /**
     * sheet页
     */
    private HSSFSheet st;
    /**
     * 表格字体格式设置
     */
    private HSSFFont tabTitleFont;
    /**
     * 表格样式
     */
    private HSSFCellStyle tabTitleStyle;
    /**
     * 表格标题行
     */
    private HSSFRow tableTitlerow;
    /**
     * 表格标题单元格
     */
    private HSSFCell tableCell;
    /**
     * 单元格的空间位置
     */
    private CellRangeAddress cellRangeAddress;
    /**
     * 表头样式
     */
    private HSSFCellStyle tabHeadStyle;
    /**
     * 表格字体格式设置
     */
    private HSSFFont tabHeadFont;
    /**
     * 表头行
     */
    private HSSFRow tableHeadRow;
    /**
     * 工作薄自适应宽度倍数因子
     */
    private final double SHEETWIDTHMULTIPLEFACTOR = 1.1211;
    /**
     * 表格数据样式
     */
    private HSSFCellStyle tabDataStyle;
    /**
     * Excel文档对象
     */
    private WritableWorkbook wbk;
    /**
     * 文件
     */
    private File file;
    /**
     * 工作簿
     */
    private WritableSheet ws;

    /**
     * @param sheetName               sheet页的名字
     * @param tableTitle              表格标题名
     * @param headTableColumnsNameArr 表头列名数组
     * @param queryList               查询list
     * @param columnNameArr           字段名数组
     * @functionName POIExcelUtils
     * @description 构造器, 初始化一些有用的类
     * @author yzh
     * @date 2018-10-14
     */
    public <T> MultiExcelUtil(String sheetName, String tableTitle, String[] headTableColumnsNameArr, List<T> queryList, String[] columnNameArr) {
        wb = new HSSFWorkbook();
        /**创建一个sheet页*/
        st = wb.createSheet(sheetName);
        /**表格标题样式**/
        tabTitleStyle = wb.createCellStyle();
        /**表格字体格式*/
        tabTitleFont = wb.createFont();
        tabTitleFont.setFontName("Times");
        /**表格单元格的空间位置*/
        if (null != headTableColumnsNameArr && headTableColumnsNameArr.length > 0) {
            //表格标题合并的列数
            int tableTitileMergeRows = headTableColumnsNameArr.length;
            cellRangeAddress = new CellRangeAddress(0, 0, 0, tableTitileMergeRows - 1);
        } else {
            cellRangeAddress = new CellRangeAddress(0, 0, 0, 0);
        }
        /**表头样式*/
        tabHeadStyle = wb.createCellStyle();
        /**表头字体格式*/
        tabHeadFont = wb.createFont();
        tabHeadFont.setFontName("Times New Roman");
        //设置表头为sheet页的第二行
        tableHeadRow = st.createRow(1);
        /**表数据样式*/
        tabDataStyle = wb.createCellStyle();

		/*设置sheet页的样式和表格标题、定义表头等基本信息*/
        setExcelSheetStyleAndBaseInfo(tableTitle, headTableColumnsNameArr, queryList, columnNameArr);
    }

    /**
     * @param tableTitle              表格标题
     * @param headTableColumnsNameArr 表头列名数组
     * @param queryList               查询list
     * @param columnNameArr           字段名数组
     * @functionName setExcelSheetStyleAndBaseInfo
     * @description 设置sheet页的样式和表格标题、定义表头等基本信息
     * @author yzh
     * @date 2018-10-14
     */
    public <T> void setExcelSheetStyleAndBaseInfo(String tableTitle, String[] headTableColumnsNameArr, List<T> queryList, String[] columnNameArr) {
        /**1、设置表格标题格式*/
        HSSFCellStyle rnTabTitleStyle = setCellBorderStyle(tabTitleStyle);

        //设置字体
        tabTitleFont.setFontName("Times");
        //设置字体大小
        tabTitleFont.setFontHeightInPoints((short) 20);
        tabTitleFont.setBoldweight((short) 700);
        //将表格标题字体加入到表格标题样式中
        rnTabTitleStyle.setFont(tabTitleFont);
        //设置表格标题并为表格标题单元格设置样式
        setExcelSheetTabTitle(tableTitle, rnTabTitleStyle);

        /**2、设置表头格式及基本信息*/
        HSSFCellStyle rnTabHeadStyle = setCellBorderStyle(tabHeadStyle);
        //设置字体
        tabHeadFont.setFontName("Times New Roman");
        //设置字体大小
        tabHeadFont.setFontHeightInPoints((short) 12);
        //将表格标题字体加入到表头样式中
        rnTabHeadStyle.setFont(tabHeadFont);
        //设置表头列名
        setHeadTableColumns(headTableColumnsNameArr, rnTabHeadStyle);

        /**3、设置 表格数据单元格样式基本信息*/
        HSSFCellStyle rnTabDataStyle = setCellBorderStyle(tabDataStyle);
        //创建一个DataFormat对象
        HSSFDataFormat format = wb.createDataFormat();
        //设置为文本格式(可以防止单元格文本太长而溢出)
        rnTabDataStyle.setDataFormat(format.getFormat("@"));
        //设置表格数据单元格样式和内容
        setTabDataCellStyleAndContent(queryList, columnNameArr, rnTabDataStyle);

        //*初始化工作薄自适应宽度
        setSheetWidthSelfAdaption(headTableColumnsNameArr);
    }

    /**
     * @param list           查询list
     * @param columnNameArr  字段数组
     * @param rnTabDataStyle 表格数据样式
     * @functionName setTabDataCellStyleAndContent
     * @description 设置表格数据单元格样式和内容
     * @author yzh
     * @date 2018-10-14
     */
    public <T> void setTabDataCellStyleAndContent(List<T> list, String[] columnNameArr, HSSFCellStyle rnTabDataStyle) {
        if (!isEmpty(columnNameArr) && columnNameArr.length > 0) {
            //表格数据从第三行开始
            int dataRow = 2;
            HSSFRow tableDataRow = null;
            //遍历出每一个对象
            for (T t : list) {
                tableDataRow = st.createRow(dataRow);
                //Field[] fArr = t.getClass().getDeclaredFields();
                Field f = null;
                HSSFCell dataCell = null;
                //防止最后一列溢出,所以这里需要比字段数目多一列
                for (int i = 0; i < columnNameArr.length + 1; i++) {
                    try {
                        if (i < columnNameArr.length) {
                            //获得私有属性域
                            f = t.getClass().getDeclaredField(columnNameArr[i]);
                            //设置私有属性可操作
                            f.setAccessible(true);
                            //获取字段的值
                            String value = f.get(t).toString();
                            dataCell = tableDataRow.createCell(i);
                            //设置单元格数据
                            dataCell.setCellValue(value);
                            //设置单元格格式
                            dataCell.setCellStyle(rnTabDataStyle);
                        } else {
                            dataCell = tableDataRow.createCell(i);
                            //设置单元格数据
                            dataCell.setCellValue("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dataRow++;
            }

        } else {
            return;
        }
    }

    /**
     * @param headTableColumnsNameArr 表头列名数组
     * @param rnTabHeadStyle          表头样式
     * @functionName setHeadTableColumns
     * @description 设置表头列名
     * @author yzh
     * @date 2018-10-14
     */
    private void setHeadTableColumns(String[] headTableColumnsNameArr, HSSFCellStyle rnTabHeadStyle) {
        if (null != headTableColumnsNameArr && headTableColumnsNameArr.length > 0) {
            for (int i = 0; i < headTableColumnsNameArr.length; i++) {
                //表头第一列所在的单元格
                HSSFCell headTableCell = tableHeadRow.createCell(i);
                //设置表头第一列所在的单元格的值
                headTableCell.setCellValue(headTableColumnsNameArr[i]);
                //设置表头单元格样式
                headTableCell.setCellStyle(rnTabHeadStyle);
            }
            return;
        } else {
            return;
        }
    }

    /**
     * @param cellStyle HSSFCellStyle对象
     * @functionName setCellBorderStyle
     * @description 设置单元格边框
     * @author yzh
     * @date 2018-10-14
     */
    @SuppressWarnings("deprecation")
    private HSSFCellStyle setCellBorderStyle(HSSFCellStyle cellStyle) {
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //下边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //左边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //上边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //右边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置内容水平居中
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置内容垂直居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return cellStyle;
    }

    /**
     * @param tabTitle        表格标题
     * @param rnTabTitleStyle 表格标题样式
     * @functionName setExcelSheetTabTitle
     * @description 设置表格标题并为表格标题单元格设置样式
     * @author yzh
     * @date 2018-10-14
     */
    private void setExcelSheetTabTitle(String tabTitle, HSSFCellStyle rnTabTitleStyle) {
        //起始行，结束行，起始列，终止列
        st.addMergedRegion(cellRangeAddress);
        //将表格标题设置在第一行（0）
        tableTitlerow = st.createRow(0);
        //选中合并后的单元格作为表格标题信息存储的那个单元格
        tableCell = tableTitlerow.createCell(0);
        //设置表格标题内容
        tableCell.setCellValue(tabTitle);
        //为表格单元格设置样式
        tableCell.setCellStyle(rnTabTitleStyle);
    }

    /**
     * @param headTableColumnsNameArr 表头列名数组
     * @functionName setSheetWidthSelfAdaption
     * @description 不定表格列的工作簿的宽度自适应
     * @author yzh
     * @date 2018-10-14
     */
    private void setSheetWidthSelfAdaption(String[] headTableColumnsNameArr) {
        if (null != headTableColumnsNameArr && headTableColumnsNameArr.length > 0) {
            //获取表头列数
            int headTableColumns = headTableColumnsNameArr.length;
            //工作薄单元格总宽度
            int sumSheetColumnWidth = 0;
            //获取Excel空白文档默认的列数
            int sheetDefaultColumnNums = 21;//st.getLastRowNum();
            //获取Excel空白文档默认的列宽，由于这里获取的单字符的长度，因此需要*256，以便与getColumnWidth的单位保持一致
            int sheetDefaultWidth = st.getDefaultColumnWidth() * 256;
            //获取Excel空白文档默认的总宽宽
            double totalSheetWidth = sheetDefaultColumnNums * sheetDefaultWidth;
            //工作薄单元格总宽度
            for (int m = 0; m < headTableColumns; m++) {
                //设置自适应列宽
                st.autoSizeColumn((short) m);
                //获取自适应列宽后的列宽
                //getColumnWidth(int columnIndex)get the width (in units of 1/256th of a character width )以一个字符的1/256的字母宽度作为一个单位
                sumSheetColumnWidth += st.getColumnWidth(m);
            }
            //获取sheet页默认宽度相对于数据部分宽度的倍数
            double rate = totalSheetWidth * 1.0 / sumSheetColumnWidth;
            //重置宽度
            for (int m = 0; m < headTableColumns; m++) {
                //重置宽度
                st.setColumnWidth(m, (int) (st.getColumnWidth(m) * rate * SHEETWIDTHMULTIPLEFACTOR));
            }
            return;
        } else {
            return;
        }
    }

    /**
     * @param headTableColumnsNameArr  表头列名数组
     * @param tableWidthMultipleFactor 工作薄自适应宽度倍数因子
     * @functionName setSheetWidthSelfAdaption
     * @description 不定表格列的工作簿宽度自适应（可自定义宽度倍数因子）
     * @author yzh
     * @date 2018-10-14
     */
    public void setSheetWidthSelfAdaption(String[] headTableColumnsNameArr, int tableWidthMultipleFactor) {
        if (null != headTableColumnsNameArr && headTableColumnsNameArr.length > 0) {
            //获取表头列数
            int headTableColumns = headTableColumnsNameArr.length;
            //工作薄单元格总宽度
            int sumSheetColumnWidth = 0;
            //获取Excel空白文档默认的列数
            int sheetDefaultColumnNums = st.getLastRowNum();
            //获取Excel空白文档默认的列宽，由于这里获取的单字符的长度，因此需要*256，以便与getColumnWidth的单位保持一致
            int sheetDefaultWidth = st.getDefaultColumnWidth() * 256;
            //获取Excel空白文档默认的总宽宽
            double totalSheetWidth = sheetDefaultColumnNums * sheetDefaultWidth;
            //工作薄单元格总宽度
            for (int m = 0; m < headTableColumns; m++) {
                //设置自适应列宽
                st.autoSizeColumn((short) m);
                //获取自适应列宽后的列宽
                //getColumnWidth(int columnIndex)get the width (in units of 1/256th of a character width )以一个字符的1/256的字母宽度作为一个单位
                sumSheetColumnWidth += st.getColumnWidth(m);
            }
            //获取sheet页默认宽度相对于数据部分宽度的倍数
            double rate = totalSheetWidth * 1.0 / sumSheetColumnWidth;
            //重置宽度
            for (int m = 0; m < headTableColumns; m++) {
                //重置宽度,如果工作薄自适应宽度倍数因子>1,则启用手工设置的因子
                if (tableWidthMultipleFactor > 1) {
                    st.setColumnWidth(m, (int) (st.getColumnWidth(m) * rate * tableWidthMultipleFactor));
                    //否则启用系统默认初始化因子
                } else {
                    st.setColumnWidth(m, (int) (st.getColumnWidth(m) * rate * SHEETWIDTHMULTIPLEFACTOR));
                }
            }
            return;
        } else {
            return;
        }
    }

    /**
     * @param exportFilePath 导出文件路径
     * @functionName excelDataExport
     * @description 导出Excel文档
     * @author yzh
     * @date 2018-10-14
     */
    public void excelDataExport(String exportFilePath) {
        File file = new File(exportFilePath);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            //调用write方法导出xls文件
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void excelDataExport(OutputStream out) {
        try {
            //调用write方法导出xls文件
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void excelDataExport(HttpServletResponse response) {
        try {
            //调用write方法导出xls文件
            response.reset();
            response.setContentType("application/octet-stream;charset=utf-8");
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("test" + ".xlsx").getBytes(), "iso-8859-1"));
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(("test" + ".xlsx").getBytes(), "iso-8859-1"));
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
//            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param obj
     * @functionName isEmpty
     * @description 判断对象是否为空
     * @author yzh
     * @date 2018-10-14
     */
    public boolean isEmpty(Object obj) {
        boolean flag = true;
        if (null != obj && !"".equals(obj)) {
            flag = false;
        }
        return flag;
    }


    /**
     * @param filePath        文件导出名
     * @param sheetName       sheet页的名字数组
     * @param tablesColumnArr 表格字段数组
     * @param list            查询list(元素为list)
     * @functionName POIExcelUtils
     * @description 构造器, 初始化一些有用的类, 支持多个sheet同时导出
     * @author yzh
     * @date 2018-10-14
     */
    @SuppressWarnings("unchecked")
    public <T> MultiExcelUtil(String filePath, String[] sheetName, String[] tablesColumnArr, List<Object> list) {
        file = new File(filePath);
        try {
            wbk = Workbook.createWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //工作簿的名字以及工作薄的位置
        for (int i = 0; i < sheetName.length; i++) {
            ws = wbk.createSheet(sheetName[i], i);
            //为每一个sheet页添加标题和内容
            setSheetTableContent(tablesColumnArr[i], ws, (List<T>) list.get(i));
        }
        //Excel操作完毕之后，关闭所有的操作资源
        try {
            //从内存中写入文件中
            wbk.write();
            //关闭资源，释放内存
            wbk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param tableColumns 表头列拼接字符串
     * @param ws           sheet对象
     * @param list         查询list(元素为list)
     * @functionName setSheetTableContent
     * @description 设置sheet表格样式及内容
     * @author yzh
     * @date 2018-10-14
     */
    private <T> void setSheetTableContent(String tableColumns, WritableSheet ws, List<T> list) {
        if (!isEmpty(tableColumns)) {
            String[] tableColumnsArr = tableColumns.split(",");
            for (int i = 0; i < tableColumnsArr.length; i++) {
                try {
                    WritableFont wfc = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    WritableCellFormat wcfFC = new WritableCellFormat(wfc);
                    wcfFC.setBackground(Colour.GRAY_25);
                    wcfFC.setAlignment(Alignment.CENTRE);
                    Label label = new Label(i, 0, tableColumnsArr[i], wcfFC);
                    ws.setColumnView(i, 20); //设置列宽
                    ws.addCell(label);  //添加标题
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Field[] fArr = null;
            T t = null;
            //下面开始添加单元格
            for (int m = 0; m < list.size(); m++) {
                t = list.get(m);
                //获得私有属性域
                fArr = t.getClass().getDeclaredFields();
                //防止最后一列溢出,所以这里需要比字段数目多一列
                for (int j = 0; j < fArr.length + 1; j++) {
                    if (j < fArr.length) {
                        try {
                            Field f = fArr[j];
                            //设置私有属性可操作
                            f.setAccessible(true);
                            //获取字段的值
                            String value = f.get(t).toString();
                            //这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
                            Label labelC = new Label(j, m + 1, value);
                            //将生成的单元格添加到工作表中
                            ws.addCell(labelC);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Label labelC = new Label(j, m + 1, "");
                        //将生成的单元格添加到工作表中
                        try {
                            ws.addCell(labelC);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * @param sheetNameArr             sheet页的名字数组
     * @param tableTitleArr            表格标题名数组
     * @param headTablesColumnsNameArr 多个表头列名数组
     * @param queryList                查询list的集合
     * @param tablesColumnNameArr      字段名拼接串的的数组
     * @functionName POIExcelUtils
     * @description 构造器, 初始化一些有用的类, 同时导出多个sheet页
     * @author yzh
     * @date 2018-10-14
     */
    @SuppressWarnings("unchecked")
    public <T> MultiExcelUtil(String[] sheetNameArr, String[] tableTitleArr, String[] headTablesColumnsNameArr, List<Object> queryList, String[] tablesColumnNameArr) {
        wb = new HSSFWorkbook();
        if (!isEmpty(sheetNameArr) && sheetNameArr.length > 0 && !isEmpty(tableTitleArr) && tableTitleArr.length > 0) {
            for (int i = 0; i < sheetNameArr.length; i++) {
                /**创建一个sheet页*/
                st = wb.createSheet(sheetNameArr[i]);
                /**表格标题样式**/
                tabTitleStyle = wb.createCellStyle();
                /**表格字体格式*/
                tabTitleFont = wb.createFont();
                tabTitleFont.setFontName("Times");
                if (!isEmpty(headTablesColumnsNameArr) && headTablesColumnsNameArr.length > 0 && !isEmpty(tablesColumnNameArr) && tablesColumnNameArr.length > 0) {
                    System.out.println("---------i--------" + i);
                    String[] headTableColumnsNameArr = headTablesColumnsNameArr[i].split(",");
                    /**表格单元格的空间位置*/
                    if (null != headTableColumnsNameArr && headTableColumnsNameArr.length > 0) {
                        //表格标题合并的列数
                        int tableTitileMergeRows = headTableColumnsNameArr.length;
                        cellRangeAddress = new CellRangeAddress(0, 0, 0, tableTitileMergeRows - 1);
                    } else {
                        cellRangeAddress = new CellRangeAddress(0, 0, 0, 0);
                    }
                    /**表头样式*/
                    tabHeadStyle = wb.createCellStyle();
                    /**表头字体格式*/
                    tabHeadFont = wb.createFont();
                    tabHeadFont.setFontName("Times New Roman");
                    //设置表头为sheet页的第二行
                    tableHeadRow = st.createRow(1);
                    /**表数据样式*/
                    tabDataStyle = wb.createCellStyle();
                    //获取每个sheet页的列名
                    String[] tableColumnNameArr = tablesColumnNameArr[i].split(",");
                    /**设置sheet页的样式和表格标题、定义表头等基本信息*/
                    setExcelSheetStyleAndBaseInfo(tableTitleArr[i], headTableColumnsNameArr, (List<T>) queryList.get(i), tableColumnNameArr);
                }
            }
        }
    }


    /**
     * sheetNameArr  sheet名
     * tableTitleArr 标题名
     * headTablesColumnsNameArr 属性中文 "学员名称,部门名称,完成度"
     * tablesColumnNameArr      属性英文 "name,deptName,schedule"
     * mixResultMap
     */
    public static void exportMultiExcel(String[] sheetNameArr, String[] tableTitleArr, String[] headTablesColumnsNameArr, String[] tablesColumnNameArr, Map<String, List> mixResultMap, OutputStream out) {
        String datePattern = null;
        if (datePattern == null) datePattern = ExcelConstant.DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        //表头样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setFontName("Times");
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBoldweight((short) 700);
        titleStyle.setFont(titleFont);
        // 列头样式
        CellStyle headerStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        headerStyle.setDataFormat(format.getFormat("@"));
        headerStyle.setFillPattern(HSSFCellStyle.NO_FILL);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setFontName("Times New Roman");
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);

        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat formatcell = workbook.createDataFormat();
        cellStyle.setDataFormat(formatcell.getFormat("@"));
        org.apache.poi.ss.usermodel.Font cellrFont = workbook.createFont();
        cellrFont.setFontName("Times");
        cellrFont.setFontHeightInPoints((short) 12);
        cellStyle.setFont(cellrFont);

        int colWidth = 0;

        for (int i = 0; i < sheetNameArr.length; i++) {
            String title = tableTitleArr[i];
            // 生成一个(带标题)表格
            String sheetName = sheetNameArr[i];
            SXSSFSheet sheet = workbook.createSheet(sheetName);
            DataValidationHelper helper = sheet.getDataValidationHelper();
            Integer columnCount = headTablesColumnsNameArr[i].split(",").length;
            for (int j = 0; j < columnCount; j++) {
                sheet.setDefaultColumnStyle(i, cellStyle);
            }
            int minBytes = colWidth < ExcelConstant.DEFAULT_COLOUMN_WIDTH ? ExcelConstant.DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
            int[] arrColWidth = new int[columnCount];
            // 产生表格标题行,以及设置列宽
            String[] properties = new String[columnCount];
            String[] headers = new String[columnCount];
            int ii = 0;
            String[] tableColumnNameArray = tablesColumnNameArr[i].split(",");
            String[] tableColumnAttrArray = headTablesColumnsNameArr[i].split(",");
            // headMap.put("questionType","题型");
            for (int ss = 0; ss < columnCount; ss++) {
                String fieldName = tableColumnNameArray[ss];
                properties[ii] = fieldName;
                headers[ii] = tableColumnAttrArray[ss];
                int bytes = fieldName.getBytes().length;
                arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
                sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
                ii++;
            }
            List<Object> dataList = mixResultMap.get(sheetName);
            int rowIndex = 0;
            if (CollectionUtils.isEmpty(dataList)) {
                if (rowIndex == 0) {
                    if (rowIndex != 0) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示
                    SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                    titleRow.createCell(0).setCellValue(title);
                    titleRow.getCell(0).setCellStyle(titleStyle);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnCount - 1));

                    SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                    for (int ix = 0; ix < headers.length; ix++) {
                        headerRow.createCell(ix).setCellValue(headers[ix]);
                        headerRow.getCell(ix).setCellStyle(headerStyle);
                    }
                    rowIndex = 2;//数据内容从 rowIndex=2开始
                }
            } else {
                for (Object obj : dataList) {
                    if (rowIndex == 65535 || rowIndex == 0) {
                        if (rowIndex != 0) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示
                        SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                        titleRow.createCell(0).setCellValue(title);
                        titleRow.getCell(0).setCellStyle(titleStyle);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnCount - 1));

                        SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                        for (int ix = 0; ix < headers.length; ix++) {
                            headerRow.createCell(ix).setCellValue(headers[ix]);
                            headerRow.getCell(ix).setCellStyle(headerStyle);

                        }
                        rowIndex = 2;//数据内容从 rowIndex=2开始
                    }
                    JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
                    SXSSFRow dataRow = sheet.createRow(rowIndex);
                    for (int iy = 0; iy < properties.length; iy++) {
                        SXSSFCell newCell = dataRow.createCell(iy);
                        Object o = jo.get(properties[iy]);
                        String cellValue = "";
                        if (o == null) cellValue = "";
                        else if (o instanceof Date) cellValue = new SimpleDateFormat(datePattern).format(o);
                        else if (o instanceof Float || o instanceof Double)
                            cellValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        else cellValue = o.toString();
                        newCell.setCellValue(cellValue);
                    }
                    rowIndex++;
                }
            }
        }
        try {
            workbook.write(out);
            workbook.close();
            workbook.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        String exportFilePath3 = "/Users/zezhouyang/Desktop/export_multi.xls";
//        String[] sheetNameArr = {"已完成", "未完成"};
//        String[] tableTitleArr = {"学员完成度_已完成", "学员完成度_未完成"};
//        String[] headTablesColumnsNameArr = {"学员名称,部门名称,完成度,正确率,完成时间", "学员名称,部门名称,完成度,最后答题时间"};
//        String[] tablesColumnNameArr = {"name,deptName,schedule,accuracy,finishTime", "name,deptName,schedule,finishTime"};
//        List<Object> list = new ArrayList<Object>();
//        List<UserXLibraryCountVO> resultList = new ArrayList<>();
//        //public UserXLibraryCountVO(Integer id, String name, String deptName, String schedule, String accuracy, String finishTime
//        //resultList.add(new UserXLibraryCountVO("杨文倩","策略运营中心","100%","80%","2018-10-02 10:30:00"));
//        //resultList.add(new UserXLibraryCountVO("长老","策略运营中心","100%","85%","2018-10-02 10:29:00"));
//        //public UserXLibraryCountVO(Integer id, String name, String deptName, String schedule, String finishTime)
//        List<UserXLibraryCountVO> resultList2 = new ArrayList<>();
//        resultList2.add(new UserXLibraryCountVO("陶琪", "策略运营中心", "70%", "2018-10-02 10:22:00"));
//        resultList2.add(new UserXLibraryCountVO("阿杜", "策略运营中心", "65%", "2018-10-02 10:23:00"));
//        list.add(resultList);
//        list.add(resultList2);
//        MultiExcelUtil poiExcel = new MultiExcelUtil(sheetNameArr, tableTitleArr, headTablesColumnsNameArr, list, tablesColumnNameArr);
//        poiExcel.excelDataExport(exportFilePath3);
//        System.out.println();

        String str1 = "1,2,3,4,5";
        System.out.println(str1.split(",").length);
    }

}
