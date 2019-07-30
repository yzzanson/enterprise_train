package com.enterprise.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/5 下午7:46
 */
public class LotteryUtil {

    public static void main(String[] args) {/**
     * 先来一个简单通用的解决方案
     * 需求：生成A(一等奖):20%;B(二等奖):30%;C(不中奖):50%
     */
        double num;
        Map<String ,Integer> confirmMap = new HashMap<>();
        int countA = 0;
        int countB = 0;
        int countC = 0;
        for(int i = 0; i < 10000; i++){
            num = Math.random() * 100;
            if(num < 20){
                countA++;
            }else if(num >= 20 && num < 50){
                countB++;
            }else{
                countC++;
            }
        }
        confirmMap.put("A", countA);
        confirmMap.put("B", countB);
        confirmMap.put("C", countC);
        int total = 0;
        for(Map.Entry<String, Integer> cmap : confirmMap.entrySet()){
            total += cmap.getValue();
        }
        System.out.println("总计："+total);
        NumberFormat nfmt = NumberFormat.getInstance();// 创建一个数值格式化对象
        nfmt.setMinimumIntegerDigits(2);// 设置精确到小数点后2位
        for(Map.Entry<String, Integer> omap : confirmMap.entrySet()){
            System.out.print(omap.getKey()+":"+omap.getValue());
            System.out.println("。。。占比为" + nfmt.format((double)omap.getValue()/total * 100) +"%");
        }
    }
}
