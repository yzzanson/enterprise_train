package com.enterprise.thread;

/**
 * @Description 同步单个题库(官方)全部企业
 * @Author zezhouyang
 * @Date 18/6/11 下午3:23
 */
@Deprecated
public class SynchronizePublicLibrary implements Runnable  {

    /*
    private static final Logger logger = LoggerFactory.getLogger(SynchronizePublicLibrary.class);

    private Integer libraryId;

    public SynchronizePublicLibrary(Integer libraryId) {
        this.libraryId = libraryId;
    }

    private CompanyXLibraryService companyXLibraryService = SpringContextHolder.getBean(CompanyXLibraryService.class);
    private IsvTicketsService isvTicketsService = SpringContextHolder.getBean(IsvTicketsService.class);
    private CompanyInfoService companyInfoService = SpringContextHolder.getBean(CompanyInfoService.class);
    private UserXCompanyService userXCompanyService = SpringContextHolder.getBean(UserXCompanyService.class);
    private UserXLibraryService userXLibraryService = SpringContextHolder.getBean(UserXLibraryService.class);
    private QuestionsLibraryService questionsLibraryService = SpringContextHolder.getBean(QuestionsLibraryService.class);
    private StudyRemindService studyRemindService = SpringContextHolder.getBean(StudyRemindService.class);
    */
    @Override
        public void run() {
        /*
        List<CompanyXLibraryEntity> updateCompanyXLibraryList = new ArrayList<>();
        List<CompanyXLibraryEntity> addCompanyXLibraryList = new ArrayList<>();
        //遍历当前公司将其所有员工获取该题库
        List<CompanyInfoEntity> companyList = companyInfoService.getAllCompanys();
        for (int i = 0; i < companyList.size(); i++) {
            Integer companyId = companyList.get(i).getId();
            String compName = companyList.get(i).getName();
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCompanyId(companyId);
            String corpId = isvTicketsEntity.getCorpId();
            CompanyXLibraryEntity companyLibraryInDB = companyXLibraryService.findCompanyXLibrary(companyId, libraryId);
            if(companyLibraryInDB!=null && companyLibraryInDB.getStatus().equals(StatusEnum.DELETE.getValue())){
                companyLibraryInDB.setStatus(StatusEnum.OK.getValue());
                updateCompanyXLibraryList.add(companyLibraryInDB);
            }else if(companyLibraryInDB==null){
                addCompanyXLibraryList.add(new CompanyXLibraryEntity(companyId,libraryId,0,StatusEnum.OK.getValue(),new Date(),new Date()));
            }
            List<UserXLibraryEntity> addUserXLibraryList =  addUserLibrarys(corpId,libraryId);
            List<StudyRemindEntity> addStudyRemindList = addStudyRemind(corpId,companyId,libraryId);
            if(CollectionUtils.isNotEmpty(addUserXLibraryList)){
                userXLibraryService.batchInsertUserXLibrary(addUserXLibraryList);
                questionsLibraryService.addLibraryUseCount(libraryId,addUserXLibraryList.size());
            }
            if(CollectionUtils.isNotEmpty(addStudyRemindList)){
                studyRemindService.batchInsertStudyRemind(addStudyRemindList);
            }
            String content = compName+"新增了"+addUserXLibraryList.size()+"用户题库,新增了"+addStudyRemindList.size()+"学习提醒记录!";
            logger.info(content);
            OAMessageUtil.sendTextMsgToDept(content);
        }
        if(CollectionUtils.isNotEmpty(addCompanyXLibraryList)){
            companyXLibraryService.batchInsert(addCompanyXLibraryList);
        }
        if(CollectionUtils.isNotEmpty(updateCompanyXLibraryList)){
            companyXLibraryService.batchupdate(updateCompanyXLibraryList);
        }

        //更新题库的使用次数
        Integer libraryUseCount = companyXLibraryService.findCompanyLibraryCount(libraryId);
        questionsLibraryService.updateUseCount(libraryId,libraryUseCount);
    }

    List<UserXLibraryEntity> addUserLibrarys(String corpId,Integer libraryId){
        List<UserXLibraryEntity> userXLibraryList = new ArrayList<>();
        List<UserVO> userCompanyList = userXCompanyService.getUserByCorpId(corpId);
        List<Integer> userLibraryList = userXLibraryService.getUserByCorpIdAndLibraryId(corpId, libraryId);
        Set<Integer> userLibrarySet = new HashSet<>(userLibraryList);
        for (int i = 0; i < userCompanyList.size(); i++) {
            Integer userId = userCompanyList.get(i).getId();
            if(!userLibrarySet.contains(userId)){
                userXLibraryList.add(new UserXLibraryEntity(userId,libraryId,0,null,StatusEnum.OK.getValue(),new Date(),new Date()));
                userLibrarySet.add(userId);
            }
        }
        return userXLibraryList;
        */
    }

    /*
    List<StudyRemindEntity> addStudyRemind(String corpId,Integer companyId,Integer libraryId){
        List<StudyRemindEntity> studyRemindList = new ArrayList<>();
        List<UserVO> userCompanyList = userXCompanyService.getUserByCorpId(corpId);
        List<Integer> remindUserList = studyRemindService.getRemindUserIds(companyId, libraryId,null);
        Set<Integer> remindUserSet = new HashSet<>(remindUserList);
        for (int i = 0; i < userCompanyList.size(); i++) {
            Integer userId = userCompanyList.get(i).getId();
            if(!remindUserSet.contains(userId)){
                studyRemindList.add(new StudyRemindEntity(companyId,libraryId,userId,StatusEnum.OK.getValue(),new Date(),new Date()));
                remindUserSet.add(userId);
            }
        }
        return studyRemindList;
    }
    */
}
