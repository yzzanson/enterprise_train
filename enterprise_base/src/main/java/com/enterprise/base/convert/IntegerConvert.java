package com.enterprise.base.convert;

import org.springframework.core.convert.converter.Converter;

/**
 * Created by pine on  2017/4/7 下午1:54.
 */
public class IntegerConvert implements Converter<String, Integer> {
    @Override
    public Integer convert(String str) {
        try {
            if (str.equals("null")) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.valueOf(str);
    }
}
