package com.enterprise.util.excel;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/5/3 下午11:17
 */
public class PetNameExcelDTO {


    @ExcelVOAttribute(name = "宠物名字", column = "A", isExport = true, prompt = "题干!")
    private String petName;

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
