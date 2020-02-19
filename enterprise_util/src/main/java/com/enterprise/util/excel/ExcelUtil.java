package com.enterprise.util.excel;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.enums.QuestionTypeEnum;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * ExcelUtil工具类实现功能:
 * 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
 * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
 * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能:
 * 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
 * 2.列名称可以通过注解配置.
 * 3.导出到哪一列可以通过注解配置.
 * 4.鼠标移动到该列时提示信息可以通过注解配置.
 * 5.用注解设置只能下拉选择不能随意填写功能.
 * 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
 * 本工具类以后可能还会加功能,请关注我的博客: http://blog.csdn.net/lk_blog
 */
public class ExcelUtil<T> {


    Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }


    public static List<Integer> importXSSF(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            List<Integer> resultList = new ArrayList<>();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            XSSFSheet xssfsheet = xssfWorkbook.getSheetAt(0);
            if (xssfsheet == null || xssfsheet.getLastRowNum() < 2) {
                return null;
            }
            if (xssfsheet != null) {
                //遍历行
                for (int rowNum = 1; rowNum <= xssfsheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfsheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
//                    if(xssfRow.getCell(0)!=null && xssfRow.getCell(0).getStringCellValue().equals("1（示例）")){
//                        continue;
//                    }
                    //遍历每行的每个cell
                    Integer integer = Integer.valueOf(0);
                    boolean insert = true;
//                    if (hssfRow.getLastCellNum() != 8) {
//                        throw new Exception("上传文件格式不对");
//                    }
                    for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell xssfCell = xssfRow.getCell(cellNum);
                        if (xssfCell == null) {
                            insert = false;
                            continue;
                        }
//                        xssfCell.setCellType(xssfCell.CELL_TYPE_STRING);
                        String cellValue = ExcelQuestionUtil.getValue(xssfCell);
                        if (cellNum == 0) {
                            integer = Integer.valueOf(cellValue);
                        }

                    }
                    resultList.add(integer);
                }
                return resultList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<String> importXSSFS(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            List<String> resultList = new ArrayList<>();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            XSSFSheet xssfsheet = xssfWorkbook.getSheetAt(0);
            if (xssfsheet == null || xssfsheet.getLastRowNum() < 2) {
                return null;
            }
            if (xssfsheet != null) {
                //遍历行
                for (int rowNum = 1; rowNum <= xssfsheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfsheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
//                    if(xssfRow.getCell(0)!=null && xssfRow.getCell(0).getStringCellValue().equals("1（示例）")){
//                        continue;
//                    }
                    //遍历每行的每个cell
                    Integer integer = Integer.valueOf(0);
                    boolean insert = true;
//                    if (hssfRow.getLastCellNum() != 8) {
//                        throw new Exception("上传文件格式不对");
//                    }
                    for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell xssfCell = xssfRow.getCell(cellNum);
                        if (xssfCell == null) {
                            insert = false;
                            continue;
                        }
//                        xssfCell.setCellType(xssfCell.CELL_TYPE_STRING);
                        String cellValue = ExcelQuestionUtil.getValue(xssfCell);

                        resultList.add(cellValue);
                    }
                }
                return resultList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导入excel数据
     *
     * @param sheetName 要导入的sheet名称
     * @param startRow  开始导入的行
     * @param input     要导入的excel数据流
     * @return
     */
    public List<T> importExcel(String sheetName, int startRow, InputStream input, Integer type) {
        List<T> list = new ArrayList<T>();
        try {
            Workbook book = null;
//            book = new HSSFWorkbook(input);
            if (type == 0) {
                book = new XSSFWorkbook(input);//处理2007
            } else {
                book = new HSSFWorkbook(input);//2003
            }
//            book = new XSSFWorkbook(input);//处理2007
            Sheet sheet = null;
            if (!StringUtils.isEmpty(sheetName)) {
                sheet = book.getSheet(sheetName);// 如果指定sheet名,则取指定sheet中的内容.
            }
            if (sheet == null) {
                sheet = book.getSheetAt(0);// 如果传入的sheet名不存在则默认指向第1个sheet.
            }
            //int rows = sheet.getPhysicalNumberOfRows();// 得到数据的行数
            Iterator rows = sheet.rowIterator();
            if (rows.hasNext()) {// 有数据时才处理
                Field[] allFields = clazz.getDeclaredFields();// 得到类的所有field.
                Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();// 定义一个map用于存放列的序号和field.
                for (Field field : allFields) {
                    // 将有注解的field存放到map中.
                    if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                        ExcelVOAttribute attr = field
                                .getAnnotation(ExcelVOAttribute.class);
                        int col = getExcelCol(attr.column());// 获得列号
                        // System.out.println(col + "====" + field.getName());
                        field.setAccessible(true);// 设置类的私有字段属性可访问.
                        fieldsMap.put(col, field);
                    }
                }
                while (rows.hasNext()) {
                    //Row row = sheet.getRow(i);
                    Row row = (Row) rows.next();
                    //从指定行开始处理，一般前几行为抬头
                    if (row.getRowNum() < startRow) continue;
                    T entity = null;
                    /*int cols=row.getPhysicalNumberOfCells();*/
                    //int cols = row.getLastCellNum();
                    Iterator cols = row.cellIterator();
                    while (cols.hasNext()) {
                        //String c = row.getCell(j).getStringCellValue();// 单元格中的内容.
                        //Cell cell = row.getCell(j);
                        Cell cell = (Cell) cols.next();
                        String c = cell.toString();// 单元格中的内容.
                        entity = (entity == null ? clazz.newInstance() : entity);// 如果不存在实例则新建.
                        // System.out.println(cells[j].getContents());
                        Field field = fieldsMap.get(cell.getColumnIndex());// 从map中得到对应列的field.
                        // 取得类型,并根据对象类型设置值.
                        if (field == null) continue;
                        Class<?> fieldType = field.getType();
                        if ((Integer.TYPE == fieldType)
                                || (Integer.class == fieldType)) {
                            field.set(entity, Integer.parseInt(c));
                        } else if (String.class == fieldType) {
                            field.set(entity, String.valueOf(c));
                        } else if ((Long.TYPE == fieldType)
                                || (Long.class == fieldType)) {
                            field.set(entity, Long.valueOf(c));
                        } else if ((Float.TYPE == fieldType)
                                || (Float.class == fieldType)) {
                            field.set(entity, Float.valueOf(c));
                        } else if ((Short.TYPE == fieldType)
                                || (Short.class == fieldType)) {
                            field.set(entity, Short.valueOf(c));
                        } else if ((Double.TYPE == fieldType)
                                || (Double.class == fieldType)) {
                            field.set(entity, Double.valueOf(c));
                        } else if (Character.TYPE == fieldType) {
                            if ((c != null) && (c.length() > 0)) {
                                field.set(entity, Character
                                        .valueOf(c.charAt(0)));
                            }
                        }
                        /*field.set(entity,c);*/

                    }
                    if (entity != null) {
                        list.add(entity);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @param sheetSize 每个sheet中数据的行数,此数值必须小于65536
     * @param output    java输出流
     */
    public boolean exportExcel(List<T> list, String sheetName, int sheetSize,
                               OutputStream output) {

        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
        List<Field> fields = new ArrayList<Field>();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                fields.add(field);
            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();// 产生工作薄对象

        // excel2003中每个sheet中最多有65536行,为避免产生错误所以加这个逻辑.
        if (sheetSize > 65536 || sheetSize < 1) {
            sheetSize = 65536;
        }
        double sheetNo = Math.ceil(list.size() / sheetSize);// 取出一共有多少个sheet.
        for (int index = 0; index <= sheetNo; index++) {
            HSSFSheet sheet = workbook.createSheet();// 产生工作表对象
            workbook.setSheetName(index, sheetName + index);// 设置工作表的名称.
            HSSFRow row;
            HSSFCell cell;// 产生单元格

            row = sheet.createRow(0);// 产生一行
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                ExcelVOAttribute attr = field
                        .getAnnotation(ExcelVOAttribute.class);
                int col = getExcelCol(attr.column());// 获得列号
                cell = row.createCell(col);// 创建列
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 设置列中写入内容为String类型
                cell.setCellValue(attr.name());// 写入列名

                // 如果设置了提示信息则鼠标放上去提示.
                if (!attr.prompt().trim().equals("")) {
                    setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, col, col);// 这里默认设了2-101列提示.
                }
                // 如果设置了combo属性则本列只能选择不能输入
                if (attr.combo().length > 0) {
                    setHSSFValidation(sheet, attr.combo(), 1, 100, col, col);// 这里默认设了2-101列只能选择不能输入.
                }
            }

            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                T vo = (T) list.get(i); // 得到导出对象.
                for (int j = 0; j < fields.size(); j++) {
                    Field field = fields.get(j);// 获得field.
                    field.setAccessible(true);// 设置实体类私有属性可访问
                    ExcelVOAttribute attr = field
                            .getAnnotation(ExcelVOAttribute.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(field.get(vo) == null ? ""
                                    : String.valueOf(field.get(vo)));// 如果数据存在就填入,不存在填入空格.
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        try {
            output.flush();
            workbook.write(output);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }

    }


    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @param sheetSize 每个sheet中数据的行数,此数值必须小于65536
     */
    public HSSFWorkbook exportExcel(String[] excelHeader, List<T> list, String sheetName, int sheetSize) {

        Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
        List<Field> fields = new ArrayList<Field>();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVOAttribute.class)) {
                fields.add(field);
            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();// 产生工作薄对象

        // excel2003中每个sheet中最多有65536行,为避免产生错误所以加这个逻辑.
        if (sheetSize > 65536 || sheetSize < 1) {
            sheetSize = 65536;
        }
        HSSFSheet sheet = workbook.createSheet(sheetName);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 1 + excelHeader.length - 1));

        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell1;
        cell1 = row1.createCell(1);
        cell1.setCellValue(sheetName);

        HSSFCell cell2;
        HSSFRow row2 = sheet.createRow(1);
        for (int i = 0; i < excelHeader.length; i++) {
            //创建标题列的值
            cell2 = row2.createCell(i + 1);
            cell2.setCellValue(excelHeader[i]);
            //sheet.autoSizeColumn(i);
        }

        double sheetNo = Math.ceil(list.size() / sheetSize);// 取出一共有多少个sheet.


        for (int index = 0; index <= sheetNo; index++) {
            // HSSFSheet sheet = workbook.createSheet();// 产生工作表对象
//            workbook.setSheetName(index, sheetName + index);// 设置工作表的名称.
            HSSFRow row;
            HSSFCell cell;// 产生单元格

            //row = sheet.createRow(index+2);// 产生一行
            // 写入各个字段的列头名称
            int startNo = index * sheetSize;
            int endNo = Math.min(startNo + sheetSize, list.size());
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 2);
                T vo = (T) list.get(i); // 得到导出对象.
                for (int j = 0; j < fields.size(); j++) {
                    Field field = fields.get(j);// 获得field.
                    field.setAccessible(true);// 设置实体类私有属性可访问
                    ExcelVOAttribute attr = field
                            .getAnnotation(ExcelVOAttribute.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            cell = row.createCell(getExcelCol(attr.column()));// 创建cell
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(field.get(vo) == null ? ""
                                    : String.valueOf(field.get(vo)));// 如果数据存在就填入,不存在填入空格.
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return workbook;
    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     *
     * @param col
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet         要设置的sheet.
     * @param promptTitle   标题
     * @param promptContent 内容
     * @param firstRow      开始行
     * @param endRow        结束行
     * @param firstCol      开始列
     * @param endCol        结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle,
                                          String promptContent, int firstRow, int endRow, int firstCol,
                                          int endCol) {
        // 构造constraint对象
        DVConstraint constraint = DVConstraint
                .createCustomFormulaConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(
                regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet,
                                              String[] textlist, int firstRow, int endRow, int firstCol,
                                              int endCol) {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint
                .createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(
                regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    //Web 导出excel
    public static void downloadExcelFile(String title, Map<String, String> headMap, List list, HttpServletResponse response) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ExcelUtil.exportExcelX(title, headMap, list, null, 0, os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
            response.setContentLength(content.length);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);

            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void downloadExcelMulti(String title, String[] sheetNameArr, String[] tableTitleArr, String[] headTablesColumnsNameArr, String[] tablesColumnNameArr, Map<String, List> mixResultMap, HttpServletResponse response) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            MultiExcelUtil.exportMultiExcel(sheetNameArr, tableTitleArr, headTablesColumnsNameArr, tablesColumnNameArr, mixResultMap, os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xlsx").getBytes(), "iso-8859-1"));
            response.setContentLength(content.length);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);

            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出Excel 2007 OOXML (.xlsx)格式
     *
     * @param title       标题行
     * @param headMap     属性-列头
     * @param list        数据集
     * @param datePattern 日期格式，传null值则默认 年月日
     * @param colWidth    列宽 默认 至少17个字节
     * @param out         输出流
     */
    public static void exportExcelX(String title, Map<String, String> headMap, List list, String datePattern, int colWidth, OutputStream out) {
        if (datePattern == null) datePattern = ExcelConstant.DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        //表头样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font titleFont = workbook.createFont();
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
        Font headerFont = workbook.createFont();
        headerFont.setFontName("Times New Roman");
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);

        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat formatcell = workbook.createDataFormat();
        cellStyle.setDataFormat(formatcell.getFormat("@"));
        Font cellrFont = workbook.createFont();
        cellrFont.setFontName("Times");
        cellrFont.setFontHeightInPoints((short) 12);
        cellStyle.setFont(cellrFont);

        // 生成一个(带标题)表格
        SXSSFSheet sheet = workbook.createSheet();
        DataValidationHelper helper = sheet.getDataValidationHelper();
        for (int i = 0; i < headMap.size(); i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }
        //设置列宽
        int minBytes = colWidth < ExcelConstant.DEFAULT_COLOUMN_WIDTH ? ExcelConstant.DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter
                .hasNext(); ) {
            String fieldName = iter.next();

            properties[ii] = fieldName;
            headers[ii] = headMap.get(fieldName);

            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }
        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        for (Object obj : list) {
            if (rowIndex == 65535 || rowIndex == 0) {
                if (rowIndex != 0) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示

                SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                titleRow.createCell(0).setCellValue(title);
                titleRow.getCell(0).setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

                SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                    headerRow.getCell(i).setCellStyle(headerStyle);

                }
                rowIndex = 2;//数据内容从 rowIndex=2开始
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            SXSSFRow dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++) {
                SXSSFCell newCell = dataRow.createCell(i);

                Object o = jo.get(properties[i]);
                String cellValue = "";
                if (o == null) cellValue = "";
                else if (o instanceof Date) cellValue = new SimpleDateFormat(datePattern).format(o);
                else if (o instanceof Float || o instanceof Double)
                    cellValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                else cellValue = o.toString();

                newCell.setCellValue(cellValue);

                String[] questionType = {QuestionTypeEnum.SINGLE_CHOICE.getChineseDesc(), QuestionTypeEnum.MULTI_CHOICE.getChineseDesc(), QuestionTypeEnum.TRUE_FALSE.getChineseDesc(), QuestionTypeEnum.COMPLETION.getChineseDesc()};
                creatDropDownList(sheet, helper, questionType, 1, list.size() + 10, 0, 0);
            }
            rowIndex++;
        }
        // 自动调整宽度
        /*for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }*/
        try {
            workbook.write(out);
            workbook.close();
            workbook.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创建下拉框
    private static void creatDropDownList(Sheet taskInfoSheet, DataValidationHelper helper, String[] list,
                                          Integer firstRow, Integer lastRow, Integer firstCol, Integer lastCol) {
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        //设置下拉框数据
        DataValidationConstraint constraint = helper.createExplicitListConstraint(list);
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        //处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        taskInfoSheet.addValidationData(dataValidation);
    }


    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static void main(String[] args) {

//        String filePath = "/Users/zezhouyang/Desktop/sqlresult_281865411.xlsx";
//        try {
//            List<QuestionExcelDTO> resultList = new ExcelUtil(QuestionExcelDTO.class).importExcel("商业小知识", 0, filePath);
//            List<QuestionExcelDTO2> resultList = new ExcelUtil(QuestionExcelDTO2.class).importExcel("", 1, filePath);
//            for (int i = 0; i < resultList.size(); i++) {
//                QuestionExcelDTO2 excelDTO = resultList.get(i);
//
//                System.out.print(excelDTO.toString());
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String filePath = "/Users/zezhouyang/Desktop/sqlresult_28193391111.xlsx";


        String filePath = "/Users/zezhouyang/Desktop/乐青市教育局/sys_org.xls";
        try {
            File file = new File(filePath);
            InputStream input = new FileInputStream(file);
//            List<QuestionExcelDTO> resultList = new ExcelUtil(QuestionExcelDTO.class).importExcel("商业小知识", 0, filePath);
//            List<Integer> resultList = new ExcelUtil(Integer.class).importXSSF(file);
            List<LeQIngExcelDTO> resultList = new ExcelUtil(LeQIngExcelDTO.class).importExcel("", 0, input, 1);
//            List<QuestionExcelDTO> resultList = new ExcelUtil(LeQIngExcelDTO.class).importExcel("", 0, filePath);
            String str = "";
            String sourceStr = "";
            for (int i = 0; i < resultList.size(); i++) {
                LeQIngExcelDTO leQIngExcelDTO = resultList.get(i);
                StringBuffer sbf = new StringBuffer();
                sbf.append("insert into lq_department(id , parent_id,name,status,data_index,data_status,data_status1) values(");
                sbf.append("'").append(leQIngExcelDTO.getId()).append("'").append(",");
                sbf.append("'").append(leQIngExcelDTO.getPid()).append("'").append(",");
                sbf.append("'").append(leQIngExcelDTO.getName()).append("'").append(",");
                sbf.append(leQIngExcelDTO.getStatus()).append(",");
                sbf.append(leQIngExcelDTO.getDataIndex()).append(",");
                sbf.append(leQIngExcelDTO.getDataStatus()).append(",");
                sbf.append(leQIngExcelDTO.getDataStatus1());
                sbf.append(");");

                System.out.println(sbf.toString());
            }
            System.out.println(sourceStr);
            System.out.println(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


//    /**
//     * <p>通过poi将csv导成excel</p>
//     * @author tw 2009-07-16
//     *
//     */
//    public static void exportCsv2Excel(String xlsPath,String csvFilePath){
//        try{
//            xlsPath = "e:/workbook.xls";
//            csvFilePath = "e:/workbook.csv";
//
//            CSVWriter writer = null;
//            File tempFile = null;
//            FileWriter fwriter = null; // 写数据
//            try{
//                tempFile = new File(csvFilePath);
//                fwriter = new FileWriter(tempFile);
//                writer = new CSVWriter(fwriter);
//            }catch(IOException ioex){
//                ioex.printStackTrace();
//            }
//            /*读取Excel文件时，首先生成一个POIFSFileSystem对象，
//             * 由POIFSFileSystem对象构造一个HSSFWorkbook，
//             * 该HSSFWorkbook对象就代表了Excel文档*/
//            POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(xlsPath));
//            HSSFWorkbook wb = new HSSFWorkbook(fs);
//            HSSFSheet sheet = wb.getSheetAt(0);
//
//
//            HSSFRow row = null;
//            HSSFCell cell = null;
//            String cellStr = "";
//
//            //循环读取行与列的值,并将值写入CSV文件
//            for(int i=0;i<=sheet.getLastRowNum();i++){
//                row = sheet.getRow(i);
//                String[] cellArray = new String[row.getLastCellNum()];
//                for(int j=0;j<row.getLastCellNum();j++){
//                    cell = row.getCell((short) j);
//                    // 判断储存格的格式
//                    if (cell == null){
//                        cellStr = "";
//                    }else {
//                        switch (cell.getCellType()) {
//                            case HSSFCell.CELL_TYPE_NUMERIC://数字格式
//                                cellStr = cell.getNumericCellValue() + "";
//                                // getNumericCellValue()会回传double值，若不希望出现小数点，请自行转型为int
//                                break;
//                            case HSSFCell.CELL_TYPE_STRING://字符格式
//                                cellStr = cell.getStringCellValue();
//                                break;
//                            // case HSSFCell.CELL_TYPE_FORMULA:
//                            // System.out.print(cell.getNumericCellValue());
//                            // //读出公式储存格计算後的值
//                            // //若要读出公式内容，可用cell.getCellFormula()
//                            // break;
//                            default://不明的格式
//                                break;
//                        }
//                    }
//                    System.out.println("-----row-"+i+"-----cell-"+j+"---:"+cellStr);
//                    cellArray[j] = cellStr;
//                }
//                writer.writeNext(cellArray);// 把Excel一行记录写入CSV文件
//            }
//
//            fwriter.flush();
//            fwriter.close();
//            writer.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
}
