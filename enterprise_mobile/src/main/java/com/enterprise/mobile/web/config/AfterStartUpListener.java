package com.enterprise.mobile.web.config;

import com.enterprise.mapper.pet.PetRandomWordMapper;
import com.enterprise.service.pet.impl.MyPetServiceImpl;
import com.enterprise.service.pet.impl.PetServiceImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/28 下午7:59
 */
@Component
public class AfterStartUpListener implements ApplicationListener<ContextRefreshedEvent>{

    private final String propertyFile = "petcopywrite.properties";

    private final String namePropertyFile = "petname.properties";

    @Resource
    private PetRandomWordMapper petRandomWordMapper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
             Properties prop = new Properties();
            MyPetServiceImpl.petCopywriteMap.clear();
            InputStream is = AfterStartUpListener.class.getClassLoader().getResourceAsStream(propertyFile);
            prop.load(is);
            for(Object key:prop.keySet()){
                String value = ((String) prop.get(key));
                String[] propertVals = value.split(";");
                MyPetServiceImpl.petCopywriteMap.put((String)key, propertVals);
            }

//            String petRandow = petRandomWordMapper.getAllPetWords();
            List<String> petRandowList = petRandomWordMapper.getPetWords();
            StringBuffer petRandomWordSbf = new StringBuffer(200);
            for(int i=0;i<petRandowList.size();i++){
                if(petRandomWordSbf.length()==0){
                    petRandomWordSbf.append(petRandowList.get(i));
                }else{
                    petRandomWordSbf.append(";").append(petRandowList.get(i));
                }
            }
            System.out.println(petRandomWordSbf.toString());
            MyPetServiceImpl.petCopywriteMap.put("random",petRandomWordSbf.toString().split(";"));

            prop.clear();
            PetServiceImpl.petNameMap.clear();
            InputStream iso = AfterStartUpListener.class.getClassLoader().getResourceAsStream(namePropertyFile);
            prop.load(iso);
            for(Object key:prop.keySet()){
                String value = ((String) prop.get(key));
                String[] propertVals = value.split(";");
                PetServiceImpl.petNameMap.put((String)key, propertVals);
            }
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
