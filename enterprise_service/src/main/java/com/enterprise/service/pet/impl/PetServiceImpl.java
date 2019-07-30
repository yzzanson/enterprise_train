package com.enterprise.service.pet.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.Page;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.PetEntity;
import com.enterprise.mapper.pet.MyPetMapper;
import com.enterprise.mapper.pet.PetMapper;
import com.enterprise.service.pet.PetService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * PetServiceImpl
 *
 * @author anson
 * @create 2018-04-02 上午09:37
 **/
@Service
public class PetServiceImpl implements PetService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static Map<String, String[]> petNameMap = new HashMap<>();

    @Resource
    private PetMapper petMapper;

    @Resource
    private MyPetMapper myPetMapper;



    @Override
    public Integer createOrUpdatePet(PetEntity petEntity) {
        if(petEntity.getId()==null ||(petEntity.getId()!=null&&petEntity.getId().equals(0)) ){
            return petMapper.createOrUpdatePet(petEntity);
        } else{
            petEntity.setUpdateTime(new Date());
            return petMapper.updatePet(petEntity);
        }
    }

    @Override
    public JSONObject getPetList(PageEntity pageEntity) {
        PageHelper.startPage(pageEntity.getPageNo(), pageEntity.getPageSize());
        PageInfo<PetEntity> pageInfo = new PageInfo<>(petMapper.getPetList());
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("list", pageInfo.getList());
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("page", new Page(pageInfo.getPrePage(), pageEntity.getPageNo(), pageInfo.getNextPage(), pageInfo.getLastPage()));
        return ResultJson.succResultJson(dataMap);
    }

    @Override
    public JSONObject getRandomPetName() {
        String[] words = petNameMap.get("names");
        Integer index = new Random().nextInt(words.length);
        if(index.equals(words.length) && index>1){
            index = words.length-1;
        }
        return ResultJson.succResultJson(words[index]);
    }

}
