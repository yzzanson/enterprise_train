package com.enterprise.util.excel;

import com.enterprise.base.enums.QuestionTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/5/28 上午10:53
 */
public class ExcelQuestionUtil {

    /**
     * 2007以后
     */
    public static List<QuestionExcelDTO> importXSSF(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            List<QuestionExcelDTO> resultList = new ArrayList<>();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            XSSFSheet xssfsheet = xssfWorkbook.getSheetAt(0);
            if (xssfsheet == null || xssfsheet.getLastRowNum() < 2) {
                return null;
            }
            if (xssfsheet != null) {
                //遍历行 rowNum=0开始
                for (int rowNum = 2; rowNum <= xssfsheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfsheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
                    if (xssfRow.getCell(0) != null && xssfRow.getCell(0).getCellType() == Cell.CELL_TYPE_STRING && xssfRow.getCell(0).equals("1（示例）")) {
                        continue;
                    }
                    //遍历每行的每个cell
                    QuestionExcelDTO questionExcelDTO = new QuestionExcelDTO();
                    for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell xssfCell = xssfRow.getCell(cellNum);
                        if (xssfCell == null) {
                            continue;
                        }
                        String cellValue = getValue(xssfCell);
                        if (cellNum == 0) {
                            Integer questionType = getQuestionType(cellValue);
                            if (questionType == null) {
                                continue;
                            }
                            questionExcelDTO.setType(questionType);
                        } else if (cellNum == 1) {
                            questionExcelDTO.setDescription(cellValue);
                        } else if (cellNum == 2) {
                            questionExcelDTO.setLabel(cellValue);
                        } else if (cellNum == 3) {
                            questionExcelDTO.setAnswer(cellValue);
                        } else if (cellNum == 4) {
                            questionExcelDTO.setOptionA(cellValue);
                        } else if (cellNum == 5) {
                            questionExcelDTO.setOptionB(cellValue);
                        } else if (cellNum == 6) {
                            questionExcelDTO.setOptionC(cellValue);
                        } else if (cellNum == 7) {
                            questionExcelDTO.setOptionD(cellValue);
                        } else if (cellNum == 8) {
                            questionExcelDTO.setAnswerDesc(cellValue);
                        }

                    }
                    resultList.add(questionExcelDTO);
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
     * 2003以前
     */
    public static List<QuestionExcelDTO> importHSSF(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            List<QuestionExcelDTO> resultList = new ArrayList<>();
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            HSSFSheet hssfsheet = hssfWorkbook.getSheetAt(0);
            if (hssfsheet == null || hssfsheet.getLastRowNum() < 3) {
                return null;
            }
            if (hssfsheet != null) {
                //遍历行
                for (int rowNum = 3; rowNum <= hssfsheet.getLastRowNum(); rowNum++) {
                    HSSFRow hssfRow = hssfsheet.getRow(rowNum);
                    if (hssfRow == null) {
                        continue;
                    }
                    if (hssfRow.getCell(0) != null && hssfRow.getCell(0).getCellType() == Cell.CELL_TYPE_STRING && hssfRow.getCell(0).equals("1（示例）")) {
                        continue;
                    }
                    //遍历每行的每个cell
                    QuestionExcelDTO questionExcelDTO = new QuestionExcelDTO();
                    for (int cellNum = 0; cellNum < hssfRow.getLastCellNum(); cellNum++) {
                        HSSFCell hssfCell = hssfRow.getCell(cellNum);
                        if (hssfCell == null) {
                            continue;
                        }
                        String cellValue = getValue(hssfCell);
                        if (cellNum == 0) {
                            Integer questionType = getQuestionType(cellValue);
                            if (questionType == null) {
                                continue;
                            }
                            questionExcelDTO.setType(questionType);
                        } else if (cellNum == 1) {
                            questionExcelDTO.setDescription(cellValue);
                        } else if (cellNum == 2) {
                            questionExcelDTO.setLabel(cellValue);
                        } else if (cellNum == 3) {
                            questionExcelDTO.setAnswer(cellValue);
                        } else if (cellNum == 4) {
                            questionExcelDTO.setOptionA(cellValue);
                        } else if (cellNum == 5) {
                            questionExcelDTO.setOptionB(cellValue);
                        } else if (cellNum == 6) {
                            questionExcelDTO.setOptionC(cellValue);
                        } else if (cellNum == 7) {
                            questionExcelDTO.setOptionD(cellValue);
                        } else if (cellNum == 8) {
                            questionExcelDTO.setAnswerDesc(cellValue);
                        }

                    }
                    resultList.add(questionExcelDTO);
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


    private static String getValue(HSSFCell hssfCell) {
        String val = "";
        switch (hssfCell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                DecimalFormat df = new DecimalFormat("0");
                val = df.format(hssfCell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                if (("" + hssfCell.getStringCellValue()).indexOf("E+") != -1 || ("" + hssfCell.getStringCellValue()).indexOf("e+") != -1 || ("" + hssfCell.getStringCellValue()).indexOf("E-") != -1
                        || ("" + hssfCell.getStringCellValue()).indexOf("e-") != -1) {
                    BigDecimal bd = new BigDecimal("" + hssfCell.getStringCellValue());
                    val = bd.toPlainString();
                } else {
                    val = "" + hssfCell.getStringCellValue();
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                Boolean val1 = hssfCell.getBooleanCellValue();
                val = val1.toString();
                break;
            case Cell.CELL_TYPE_BLANK:
                break;
            default:
                throw new RuntimeException("数据类型配置不正确");
        }
        return val;
    }

    public static String getValue(XSSFCell xssfCell) {
        String val = "";
        try {
            switch (xssfCell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    String str1 = String.valueOf(xssfCell.getNumericCellValue());
                    DecimalFormat df = new DecimalFormat("0.00");
                    val = df.format(xssfCell.getNumericCellValue());
                    if (val.endsWith(".00") && !str1.equals(".00")) {
                        val = val.substring(0, val.length() - 3);
                    }
                    if (xssfCell.getNumericCellValue() < 0.01) {
                        val = "" + xssfCell.getNumericCellValue();
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    String str = xssfCell.getStringCellValue();
                    val = "" + xssfCell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    Boolean val1 = xssfCell.getBooleanCellValue();
                    val = val1.toString();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    val = "";
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return val;
    }

    private static Integer getQuestionType(String cellValue) {
        if (cellValue.equals(QuestionTypeEnum.SINGLE_CHOICE.getChineseDesc())) {
            return QuestionTypeEnum.SINGLE_CHOICE.getType();
        } else if (cellValue.equals(QuestionTypeEnum.MULTI_CHOICE.getChineseDesc())) {
            return QuestionTypeEnum.MULTI_CHOICE.getType();
        } else if (cellValue.equals(QuestionTypeEnum.TRUE_FALSE.getChineseDesc())) {
            return QuestionTypeEnum.TRUE_FALSE.getType();
        } else if (cellValue.equals(QuestionTypeEnum.COMPLETION.getChineseDesc())) {
            return QuestionTypeEnum.COMPLETION.getType();
        }
        return null;
    }


    public static String replaceChar(String str, String char_orgi, String char_change) {
        if (str.indexOf(char_orgi) >= 0) {
            str = new String(str.replaceAll(char_orgi, char_change));
            System.out.println("str:" + str);
        }
        return str;
    }


    public static List<LeQIngExcelDTO> importLeQing(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            List<LeQIngExcelDTO> resultList = new ArrayList<>();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            XSSFSheet xssfsheet = xssfWorkbook.getSheetAt(0);
            if (xssfsheet == null || xssfsheet.getLastRowNum() < 2) {
                return null;
            }
            if (xssfsheet != null) {
                //遍历行 rowNum=0开始
                for (int rowNum = 2; rowNum <= xssfsheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfsheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
                    if (xssfRow.getCell(0) != null && xssfRow.getCell(0).getCellType() == Cell.CELL_TYPE_STRING && xssfRow.getCell(0).equals("1（示例）")) {
                        continue;
                    }
                    //遍历每行的每个cell
                    LeQIngExcelDTO leQIngExcelDTO = new LeQIngExcelDTO();
                    for (int cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
                        XSSFCell xssfCell = xssfRow.getCell(cellNum);
                        if (xssfCell == null) {
                            continue;
                        }
                        String cellValue = getValue(xssfCell);
                        if (cellNum == 0) {
                            leQIngExcelDTO.setId(cellValue);
                        } else if (cellNum == 1) {
                            leQIngExcelDTO.setPid(cellValue);
                        } else if (cellNum == 2) {
                            leQIngExcelDTO.setName(cellValue);
                        }else if (cellNum == 3) {
                            leQIngExcelDTO.setStatus(Integer.valueOf(cellValue));
                        }else if (cellNum == 4) {
                            leQIngExcelDTO.setDataIndex(Integer.valueOf(cellValue));
                        }else if (cellNum == 5) {
                            leQIngExcelDTO.setDataStatus(Integer.valueOf(cellValue));
                        }else if (cellNum == 6) {
                            leQIngExcelDTO.setDataStatus1(Integer.valueOf(cellValue));
                        }

                    }
                    resultList.add(leQIngExcelDTO);
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


    public static void main(String[] args) {
//        String str = "100000000000000000000.00";
//////        String sss  = replaceChar(str, "\\(", "\\（");
////        String sss = str.substring(0, str.length() - 2);
////        System.out.println(str);
////        System.out.println(sss);
        File file = new File("/Users/zezhouyang/Desktop/乐青市教育局/sys_org.xls");
        List<LeQIngExcelDTO> resultList = importLeQing(file);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i));
        }

    }
}
