package com.enterprise.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/8 上午10:16
 */
public class QuestionUtil {

    /**
     * 将题目解析成只有空格
     */
    public static Map<String, Object> parseDescription(String description) {
        try {
            StringBuffer descriptionBuffer = new StringBuffer();
            StringBuffer answerBuffer = new StringBuffer();
            String[] blankArray = description.split("\\[");
            for (int i = 0; i < blankArray.length; i++) {
                String blankSubString = blankArray[i];
                if (i == 0) {
                    //暂时没处理开头就是空括号的情况
                    descriptionBuffer.append(blankSubString);
                } else {
                    Integer closeBlank = blankSubString.indexOf("]");
                    if (closeBlank > 0) {
                        descriptionBuffer.append("[]");
                        descriptionBuffer.append(blankSubString.substring(closeBlank + 1));
                    } else {
                        descriptionBuffer.append(blankSubString.substring(closeBlank + 1));
                    }
                }
                if (blankSubString.indexOf("]") == 0) {
                    continue;
                } else {
                    if (i > 0) {
                        Integer closeBlank = blankSubString.indexOf("]");
                        String blankContrnt = blankSubString.substring(0, closeBlank);
                        if (answerBuffer.length() == 0) {
                            answerBuffer.append("[").append(blankContrnt).append("]");
                        } else {
                            answerBuffer.append(",").append("[").append(blankContrnt).append("]");
                        }
                    }
                }
            }
            Map<String, Object> descriptionMap = new HashMap<>();
            descriptionMap.put("description", descriptionBuffer.toString());
            descriptionMap.put("answer", answerBuffer.toString());
            return descriptionMap;
        } catch (Exception e) {
            return null;
        }
    }

    public static String parseCompleteDescription(String description, JSONArray answerArray) {
        StringBuffer descriptionBuf = new StringBuffer();
        String[] blankArray = description.split("\\[");
        for (int i = 0; i < blankArray.length; i++) {
            String subDescription = blankArray[i];
            if (i > 0) {
                String blanki = answerArray.getString(i - 1);
                if (StringUtils.isNotEmpty(blanki)) {
                    descriptionBuf.append("[").append(blanki).append(subDescription);
                }
            } else {
                descriptionBuf.append(subDescription);
            }
        }
        return descriptionBuf.toString();
    }

    public static Integer paserBlankCount(String description){
        return StringUtils.countMatches(description, "[]");
    }

    /**
     * 字符串转成jsonArray
     */
    public static JSONArray parseBlankTOJsonArray(String blank) {
        try {
            JSONArray jsonArray = new JSONArray();
            String[] stringArray = blank.split("]");
            for (int i = 0; i < stringArray.length; i++) {
                String answerX = "";
                String baseBlank = stringArray[i];
                if (baseBlank.indexOf(",[") >= 0) {
                    answerX = baseBlank.substring(baseBlank.indexOf(",[") + 2, baseBlank.length());
//                    System.out.println("1-"+xxx);
//                    answerX = baseBlank.substring(2, baseBlank.length());
                } else if (baseBlank.indexOf("、[") >= 0) {
                    answerX = baseBlank.substring(baseBlank.indexOf("、[") + 2, baseBlank.length());
//                    System.out.println("2-"+xxx);
//                    answerX = baseBlank.substring(2, baseBlank.length());
                } else if (baseBlank.indexOf("[") >= 0) {
                    answerX = baseBlank.substring(1, baseBlank.length());
//                    System.out.println("3-"+answerX);
                }
                jsonArray.add(answerX);
            }
            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * jsonArray转字符串
     */
    public static String parseJsonToString(JSONArray jsonArray) {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < jsonArray.size(); i++) {
            String jsonArrayi = (String) jsonArray.get(i);
            if (sbf.length() > 0) {
                sbf.append(",");
            }
            sbf.append("[").append(jsonArrayi).append("]");
        }
        return sbf.toString();
    }

    public static boolean answerMatchInsert(String str) {
        String regEx = "^[A-Da-d]+(,[A-Da-d]+)+$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    public static boolean answerMatch(String str) {
        String regEx = "^[A-Da-d]+(\\|[A-Da-d]+)+$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String parseRighrtWrongChoiceToJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("A", "正确");
        jsonObject.put("B", "错误");
        return jsonObject.toJSONString();
    }


    public static Integer getDivision(Integer correctCount, Integer totalCount) {
        DecimalFormat format = new DecimalFormat("0");
        //进度重新计算
        Integer schedule = totalCount.equals(0)?0:Integer.valueOf(format.format(((float) correctCount * 100 / totalCount)));
        if (schedule > 100) schedule = 100;
        return schedule;
    }

    public static String getPercentDivision(Integer a, Integer b) {
        if (b == 0) {
            return "0.0";
        }
        String result = "";
        float num = (float) a * 100 / b;
        if(num>100){
            num = 100;
        }
        DecimalFormat df = new DecimalFormat("0.0");
        result = df.format(num);
        return result;

    }

    public static String answerChange(String str) {
        StringBuffer sbf = new StringBuffer();
        str = str.toUpperCase();
        String[] splitArray = str.split(",");
        Arrays.sort(splitArray);
        for (int i = 0; i < splitArray.length; i++) {
            if (sbf.length() == 0)
                sbf.append(splitArray[i]);
            else
                sbf.append(",").append(splitArray[i]);
        }
        return sbf.toString();
    }


//    public static Integer testCheckBlankWrong(){
//
//        String str = "[春天|春],[夏天|夏],[秋天|秋],[冬天|冬]";
//        String answer = "[春],[夏],[秋],[冬]";
//        JSONArray rightAnswerArray = QuestionUtil.parseBlankTOJsonArray(str);

//        JSONArray answerArray = QuestionUtil.parseBlankTOJsonArray(answer);
//        if (answerArray == null || rightAnswerArray == null || rightAnswerArray.size() != answerArray.size()) {
//            return QuestionAnswerEnum.WRONG.getValue();
//        } else {
//            //按顺序判断
//            for (int i = 0; i < rightAnswerArray.size(); i++) {
//                String rightAnswerIndexI = rightAnswerArray.getString(i);//数据库
//                String answerIndexI = answerArray.getString(i);//用户传值
//                String[] rightAnswerArr = rightAnswerIndexI.split("\\|");
//                for (int j = 0; j < rightAnswerArr.length; j++) {
//                    if (answerIndexI.equals(rightAnswerArr[j])) {
//                        break;
//                    } else if (!answerIndexI.equals(rightAnswerArr[j]) && j == rightAnswerArr.length - 1) {
//                        return QuestionAnswerEnum.WRONG.getValue();
//                    } else {
//                        continue;
//                    }
//                }
//            }
//            return QuestionAnswerEnum.RIGHT.getValue();
//        }
//
//
//    }

    /*
      一年四季分别是:
      春|春天]、夏、
      秋|秋天]、冬
     * */
    public static void main(String[] args) {
//        Integer result = testCheckBlankWrong();
//        System.out.println(result);
//        StringBuffer descriptionBuffer = new StringBuffer();
//        StringBuffer answerBuffer = new StringBuffer();
////        String description = "一年四季分别是:[春|春天]、夏、[秋|秋天]、冬";
//        String description = "一年四季分别是:[]、[春|春天]、夏、[秋|秋天]、冬";
////        String description = "一年四季分别是:[春|春天]、[]、夏、[秋|秋天]、冬";//ok

//        String str = "[一]、[粒]、粟、[万]、[颗]、[子]";
//        JSONArray jsonArray = parseBlankTOJsonArray(str);
//        System.out.println(jsonArray.toJSONString());
//        System.out.println(jsonArray.size());


//        String description = "一年四季分别是:、[]、夏、[]、冬";//ok
        String description = "优惠券领取方式有[]、[]";//ok
        Integer coutnMatch = StringUtils.countMatches(description, "[]");
//        Integer result = paserBlankCount(description);
        System.out.println(coutnMatch);

//        Map<String,Object> resultMap = parseDescription(description);
//        String descriptionm = (String) resultMap.get("description");
//        String answer = (String) resultMap.get("answer");
//        String answer = "[点击领取|一键领取]、[一键领取|点击领取]";
//        JSONArray answerJson = parseBlankTOJsonArray(answer);
////        System.out.println(answer);
//        System.out.println(answerJson.toJSONString());


//        String str = "[春天|春],[夏天|夏],[秋天|秋],[冬天|冬]";
//        JSONArray jsonArray = parseBlankTOJsonArray(str);
//        System.out.println(jsonArray.toJSONString());
//        String result = parseJsonToString(jsonArray);
//        System.out.println(result);
//        String multiAnswer = "A|B|C";
//        Boolean result = answerMatch(multiAnswer);
//        System.out.println(result);
//        String multiAnswer = "A";
//        Boolean result2 = answerMatchInsert(multiAnswer);
//        System.out.println(result2);

//        Integer in = getSchedule(1,3);
//        System.out.println(in);
////        Integer result = getSchedule(3,10);
//        String result = getPercentDivision(1, 61);
//        System.out.println(result);
//        System.out.println(result);
//        String noFinishPercent = String.valueOf(new BigDecimal(100).subtract(new BigDecimal(result)));
//        System.out.println(noFinishPercent);

        //一年四季分别是:、[]、夏、[]、冬
        /*
        String description = "一年四季分别是:、[]、夏、[]、冬";//ok
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("春|春天");
        jsonArray.add("秋|秋天");

        StringBuffer descriptionBuf = new StringBuffer();
        String[] blankArray = description.split("\\[");
        for (int i = 0; i < blankArray.length; i++) {
            String subDescription = blankArray[i];
            if(i>0){
                String blanki = jsonArray.getString(i-1);
                if(StringUtils.isNotEmpty(blanki)){
                    descriptionBuf.append("[").append(blanki).append(subDescription);
                }
            }else{
                descriptionBuf.append(subDescription);
            }
        }
        System.out.println(descriptionBuf.toString());
        */

//        String str = "[\"香燐\",\"鬼灯水月|水月\"]";
//        JSONArray jsonArray = JSONArray.parseArray(str);
//        System.out.println(jsonArray);

    }


}
