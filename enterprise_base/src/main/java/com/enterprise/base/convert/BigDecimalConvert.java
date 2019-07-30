package com.enterprise.base.convert;

import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * Created by pine on  2017/4/7 上午11:30.
 */
public class BigDecimalConvert implements Converter<String, BigDecimal> {
    @Override
    public BigDecimal convert(String str) {
        try {
            if (str.equals("null")) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(str);
    }
}
