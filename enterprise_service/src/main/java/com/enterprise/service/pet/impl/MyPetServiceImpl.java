package com.enterprise.service.pet.impl;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.MobileLoginUser;
import com.enterprise.base.common.PetConstant;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.PetCopyWriteEnum;
import com.enterprise.base.enums.PetFoodGainEnum;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.vo.MyPetVO;
import com.enterprise.base.vo.MyPetVONew;
import com.enterprise.base.vo.UserPetVO;
import com.enterprise.mapper.companyInfo.CompanyInfoMapper;
import com.enterprise.mapper.pet.MyPetMapper;
import com.enterprise.mapper.pet.PetWeightMapper;
import com.enterprise.mapper.petFood.PetFoodDetailMapper;
import com.enterprise.mapper.petFood.PetFoodMapper;
import com.enterprise.mapper.petFood.PetFoodPlateMapper;
import com.enterprise.mapper.users.UserMapper;
import com.enterprise.mapper.users.UserXBehaviorMapper;
import com.enterprise.mapper.users.UserXCompanyMapper;
import com.enterprise.mapper.users.UserXOpenMapper;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.pet.MyPetService;
import com.enterprise.util.AssertUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * PetServiceImpl
 *
 * @author anson
 * @create 2018-04-02 上午09:37
 **/
@Service
public class MyPetServiceImpl implements MyPetService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static Map<String, String[]> petCopywriteMap = new HashMap<>();

    @Resource
    private MyPetMapper myPetMapper;

    @Resource
    private PetWeightMapper petWeightMapper;

    @Resource
    private UserXOpenMapper userXOpenMapper;

    @Resource
    private UserXBehaviorMapper userXBehaviorMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CompanyInfoMapper companyInfoMapper;

    @Resource
    private UserXCompanyMapper userXcompanyMapper;

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private PetFoodPlateMapper petFoodPlateMapper;

    @Resource
    private PetFoodDetailMapper petFoodDetailMapper;

    @Resource
    private PetFoodMapper petFoodMapper;

    //初始化的饲料数
    private Integer PET_INIT_FOOD_COUNT = PetConstant.INIT_COUNT;

    @Override
    public Integer createOrUpdatePet(MyPetEntity myPetEntity) {
        if (myPetEntity.getId() == null || (myPetEntity.getId() != null && myPetEntity.getId().equals(0))) {
            AssertUtil.isTrue(myPetEntity.getPetName().length() <= 6, "宠物名字过长");
            MyPetVO myPetVO = myPetMapper.getMyPet(myPetEntity.getUserId());
            if (myPetVO != null) {
                myPetEntity.setId(myPetVO.getId());
                return myPetMapper.updateMyPet(myPetEntity);
            } else {
                //初始化宠物时,给其100g猫粮
                Integer userId = myPetEntity.getUserId();
                List<UserXCompany> userCompanyList = userXcompanyMapper.getUserCompanyList(userId);
                for (int i = 0; i < userCompanyList.size(); i++) {
                    String corpId = userCompanyList.get(i).getCorpId();
                    Integer companyId = isvTicketsService.getIsvTicketByCorpId(corpId).getCompanyId();
                    PetFoodDetailEntity petFoodDetailInDB =  petFoodDetailMapper.getByCompanyAndUserId(companyId, myPetEntity.getUserId());
                    Boolean isExist =true;
                    if(petFoodDetailInDB==null){
                        petFoodDetailInDB = new PetFoodDetailEntity(companyId,userId, PetFoodGainEnum.INIT.getValue(),PET_INIT_FOOD_COUNT,new Date(),new Date());
                        petFoodDetailMapper.createPetFoodDetail(petFoodDetailInDB);
                        isExist = false;
                    }
                    PetFoodEntity petFoodInDB =  petFoodMapper.getPetFood(companyId, myPetEntity.getUserId());
                    if(petFoodInDB==null){
                        petFoodInDB = new PetFoodEntity(companyId,userId,PET_INIT_FOOD_COUNT,new Date(),new Date());
                        petFoodMapper.createPetFood(petFoodInDB);
                    }else if(petFoodInDB!=null && !isExist){
                        Integer foodCount = petFoodInDB.getFoodCount()+PET_INIT_FOOD_COUNT;
                        petFoodInDB.setFoodCount(foodCount);
                        petFoodMapper.updatePetFood(petFoodInDB);
                    }
                }
                myPetEntity.setLevel(0);
                myPetEntity.setExperienceValue(0);
                myPetEntity.setPhysicalValue(0);
                myPetEntity.setStatus(StatusEnum.OK.getValue());
            }
            AssertUtil.notNull(myPetEntity.getPetName(), "宠物名不能为空!");
            return myPetMapper.createOrUpdateMyPet(myPetEntity);
        } else {
            myPetEntity.setUpdateTime(new Date());
            return myPetMapper.updateMyPet(myPetEntity);
        }
    }

    @Override
    public Integer update(String name) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
//        myPetMapper.createOrUpdateMyPet(new MyPetEntity())
        MyPetVO petVO = myPetMapper.getMyPet(mobileLoginUser.getUserID());
        MyPetEntity myPetEntity = new MyPetEntity(petVO.getId(),name);
        return myPetMapper.updateMyPet(myPetEntity);
    }

    @Override
    public JSONObject getMyPet(Integer userId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
//        MyPetVO myPetVO = myPetMapper.getMyPet(userId);
        MyPetVONew myPetVO = myPetMapper.getMyPetNew(userId);
        String uName = userMapper.getNameById(userId);
        //获取宠物体重
        if (myPetVO == null) {
            return ResultJson.errorResultJson("请先领养宠物!");
        }
        PetWeightEntity petWeightEntity = petWeightMapper.getPetWeight(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID());
        if (petWeightEntity != null && petWeightEntity.getWeight() != null) {
            myPetVO.setName(uName);
            myPetVO.setWeight(petWeightEntity.getWeight());
        }else if(petWeightEntity != null && petWeightEntity.getWeight() == null){
            myPetVO.setName(uName);
            myPetVO.setWeight(0);
        }else if(petWeightEntity==null){
            MyPetVO myPetVO1 =  myPetMapper.getMyPet(userId);
            Integer experienceValue = myPetVO1.getExperienceValue();
            if(experienceValue==null){
                experienceValue = 0;
            }
            PetWeightEntity petWeightInsert = new PetWeightEntity(mobileLoginUser.getCompanyID(), userId, experienceValue, new Date(), new Date());
            petWeightMapper.createPetWeight(petWeightInsert);
        }
        return ResultJson.succResultJson(myPetVO);
    }

    /**
     * 一天以上没登陆>PK文案>随机文案
     */
    @Override
    public JSONObject getPetWords(Integer userId) {
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUser();
        Integer companyId = mobileLoginUser.getCompanyID();
        if(userId==null) {
            userId = mobileLoginUser.getUserID();
        }
//        String petWord = "";
        //最近两次的登陆记录
//        List<Date> lastLogin = userXOpenMapper.getLastSeverlOpenRecords(mobileLoginUser.getCompanyID(), mobileLoginUser.getUserID(), 2);
//        UserXBehavior userXBehaviorInDB = userXBehaviorMapper.getUserXBehavior(companyId, userId, StatusEnum.DELETE.getValue(), new Date());
        //第一次登陆
        //if (lastLogin.size() <= 1) {
        //    petWord = getPetWord(userId, companyId);
        //} else {
        //是否一天以上未登录
//        if (lastLogin.size() == 2) {
//            Long intervalLoginDays = DateUtil.getDiffDays(lastLogin.get(0), lastLogin.get(1));
//            //今天没提醒过
//            if (intervalLoginDays >= 1 && userXBehaviorInDB == null) {
//                UserXBehavior userXBehaviorInDB2 = userXBehaviorMapper.getUserXBehavior(companyId, userId, StatusEnum.DELETE.getValue(), null);
//                if (userXBehaviorInDB2 == null) {
//                    userXBehaviorMapper.createUserXBehavior(new UserXBehavior(companyId, userId, 0, StatusEnum.OK.getValue(), new Date(), new Date()));
//                } else {
//                    userXBehaviorInDB2.setUpdateTime(new Date());
//                    userXBehaviorMapper.updateUserXBeahvior(userXBehaviorInDB2);
//                }
//                petWord = genPetWord(PetCopyWriteEnum.NOTLOGIN.getDesc());
//                //今天提醒过了
//            } else if (intervalLoginDays > 1 && userXBehaviorInDB != null) {
//                petWord = getPetWord(userId, companyId);
//            } else {
//                petWord = getPetWord(userId, companyId);
//            }
//        } else {
//            petWord = getPetWord(userId, companyId);
//        }
        String petWord = getPetWord(userId, companyId);
        // }
        System.out.println("---------------------------");
        System.out.println(petWord);
        System.out.println("---------------------------");
        return ResultJson.succResultJson(petWord);
    }

    @Override
    public JSONObject initWeight() {
        //迭代所有公司的用户,所有有宠物的人
        List<CompanyInfoEntity> companyAllInfoList = companyInfoMapper.getAllCompanys();
        Integer totalInsertCount = 0;
        for (int i = 0; i < companyAllInfoList.size(); i++) {
            Integer companyId = companyAllInfoList.get(i).getId();
            IsvTicketsEntity isvTicketEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
            //获取该公司下所有有宠物的用户
            List<PetWeightEntity> petWeightList = new ArrayList<>();
            List<UserPetVO> raisePetUserList = userXcompanyMapper.getUserRaisePet(isvTicketEntity.getCorpId());
            for (int j = 0; j < raisePetUserList.size(); j++) {
                UserPetVO userPetVO = raisePetUserList.get(j);
                PetWeightEntity petWeightInDB = petWeightMapper.getPetWeight(companyId, userPetVO.getUserId());
                if (petWeightInDB == null) {
                    petWeightInDB = new PetWeightEntity(companyId, userPetVO.getUserId(), userPetVO.getExperienceValue(), new Date(), new Date());
                    petWeightList.add(petWeightInDB);
                }
            }
            //批量插入
            if (CollectionUtils.isNotEmpty(petWeightList)) {
                Integer result = petWeightMapper.batchInsert(petWeightList);
                petWeightList.clear();
                totalInsertCount += result;
            }
        }
        return ResultJson.succResultJson("一共更新了" + totalInsertCount + "个结果");
    }

    /**
     * //获取pk结果,如果没则从随机宠物语录中取
     * 宠物是否有猫粮在吃,如果没则从随机没有猫粮取,有的话从随机宠物语录中取
     */
    private String getPetWord(Integer userId, Integer companyId) {
        String petWord = "";
        PetFoodPlateEntity petFoodPlateEntity = petFoodPlateMapper.getPeteFoodPlateByUser(companyId, userId);
        if(petFoodPlateEntity==null || (petFoodPlateEntity!=null && petFoodPlateEntity.getFoodCount()==null) || (petFoodPlateEntity!=null && petFoodPlateEntity.getFoodCount()!=null && petFoodPlateEntity.getFoodCount()<=0)){
            petWord = genPetWord(PetCopyWriteEnum.PET_HUNGER.getDesc());
        }else {
            petWord = genPetWord(PetCopyWriteEnum.RANDOM.getDesc());
        }
        return petWord;
    }


    /*
     List<Integer> userDeptIdList = new ArrayList<>();
        List<DepartmentEntity> departmentEntityList = departmentMapper.getDeptsByCompanyId(companyId, null);
        for (int i = 0; i < departmentEntityList.size(); i++) {
            userDeptIdList.add(departmentEntityList.get(i).getId());
        }
        ArenaResultEntity arenaResultInDB = arenaResultMapper.getNoRemindResult(userId, userDeptIdList);
        if (arenaResultInDB != null) {
            Integer petStatus = arenaResultInDB.getPetStatus();
            if (!arenaResultInDB.getUserId().equals(userId)) {
                if (petStatus != null && petStatus.equals(PetStatusEnum.OWNER_REMIND.getValue())) {
                    petStatus = PetStatusEnum.BOTH_REMIND.getValue();
                } else {
                    petStatus = PetStatusEnum.CHALL_REMIND.getValue();
                }
            } else {
                if (petStatus != null && petStatus.equals(PetStatusEnum.CHALL_REMIND.getValue())) {
                    petStatus = PetStatusEnum.BOTH_REMIND.getValue();
                } else {
                    petStatus = PetStatusEnum.OWNER_REMIND.getValue();
                }
            }
            Integer challUserId = arenaResultInDB.getUserId().equals(userId) ? arenaResultInDB.getPkUserId() : arenaResultInDB.getUserId();
            Integer challDeptId = arenaResultInDB.getUserId().equals(userId) ? arenaResultInDB.getPkUserDeptId() : arenaResultInDB.getUserDeptId();
            Integer myDeptId = arenaResultInDB.getUserId().equals(userId) ? arenaResultInDB.getUserDeptId() : arenaResultInDB.getPkUserDeptId();
            String challUserName = userMapper.getNameById(challUserId);
            String challDeptName = departmentMapper.getDeptNameById(companyId, challDeptId);
            String myDeptName = departmentMapper.getDeptNameById(companyId, myDeptId);
            //个人胜利了
            if (arenaResultInDB.getWinnerId() != null && arenaResultInDB.getWinnerId().equals(userId)) {
                if (arenaResultInDB.getIsSameDept().equals(1)) {
                    petWord = genPetWord(PetCopyWriteEnum.SINGLE_PK_WIN.getDesc());
                    petWord = new String(petWord.replace("word", challUserName));
                } else {
                    petWord = genPetWord(PetCopyWriteEnum.DEPT_PK_WIN.getDesc());
                    petWord = new String(petWord.replace("word", myDeptName));
                }
                //个人失败了
            } else {
                if (arenaResultInDB.getIsSameDept().equals(1)) {
                    petWord = genPetWord(PetCopyWriteEnum.SINGLE_PK_LOSE.getDesc());
                    petWord = new String(petWord.replace("word", challUserName));
                } else {
                    petWord = genPetWord(PetCopyWriteEnum.DEPT_PK_LOSE.getDesc());
                    petWord = new String(petWord.replace("word", challDeptName));
                }
            }
            //更新数据库
            arenaResultMapper.updateArenaResult(new ArenaResultEntity(arenaResultInDB.getId(), petStatus, new Date()));
        }
     * */

    private String genPetWord(String desc) {
        String[] words = petCopywriteMap.get(desc);
        Integer index = new Random().nextInt(words.length);
        if (index.equals(words.length) && index > 1) {
            index = words.length - 1;
        }
        return words[index];
    }

    public static void main(String[] args) {
        MyPetVONew myPetVONew = new MyPetVONew();
        myPetVONew.setWeight(80);
        myPetVONew.setName("刘嵩");
        myPetVONew.setId(32);
        myPetVONew.setPetName("Jordan");
        myPetVONew.setUserId(23);
//        String word = "没什么事是无法实现的";
//        word = word.replace("word", "6666");
        System.out.println(ResultJson.succResultJson(myPetVONew));
    }

}
