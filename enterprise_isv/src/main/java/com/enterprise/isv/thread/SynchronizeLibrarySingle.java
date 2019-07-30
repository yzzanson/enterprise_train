package com.enterprise.isv.thread;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/18 下午3:40
 */
public class SynchronizeLibrarySingle implements Runnable {
    /*

    private static final Logger logger = LoggerFactory.getLogger(SynchronizeLibrarySingle.class);

    private String corpId;

    private Integer userId;

    public SynchronizeLibrarySingle(String corpId, Integer userId) {
        this.corpId = corpId;
        this.userId = userId;
    }

    private IsvTicketsService isvTicketsService = SpringContextHolder.getBean(IsvTicketsService.class);
    private UserXCompanyService userXCompanyService = SpringContextHolder.getBean(UserXCompanyService.class);
    private UserXLibraryService userXLibraryService = SpringContextHolder.getBean(UserXLibraryService.class);
    private QuestionsLibraryService questionsLibraryService = SpringContextHolder.getBean(QuestionsLibraryService.class);
    private StudyRemindService studyRemindService = SpringContextHolder.getBean(StudyRemindService.class);
    */
    @Override
    public void run() {
        /*
        try {
            long begin = System.currentTimeMillis();
            IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
            Integer companyId = isvTicketsEntity.getCompanyId();
//            logger.info(corpId + "用户同步结束 >" + (System.currentTimeMillis() - begin) + "ms");
            //先查看公司是否有这几套题库
            //Integer companyId, Integer subject, Integer status, Integer defaultFlag
            Integer adminUserId = userXCompanyService.getAdmin(corpId);
            List<UserXLibraryEntity> addUserXLibraryList = new ArrayList<>();
            List<StudyRemindEntity> addStudyRemind = new ArrayList<>();
            List<StudyRemindEntity> updateStudyRemind = new ArrayList<>();
            //获取通用的默认题库
            List<QuestionsLibraryEntity> questionsLibraryList = questionsLibraryService.getQuestionLibrarys(new QuestionsLibraryEntity(companyId, SubjectEnum.ENTERPRISE.getValue(), 1, 1));

            if (CollectionUtils.isNotEmpty(questionsLibraryList)) {
                for (QuestionsLibraryEntity questionsLibraryEntity : questionsLibraryList) {
                    Integer libraryId = questionsLibraryEntity.getId();
                    UserXLibraryEntity userXLibraryEntity = userXLibraryService.getByUserIdAndLibraryId(companyId,userId, libraryId);
                    if (userXLibraryEntity == null) {
                        userXLibraryEntity = new UserXLibraryEntity(userId, libraryId, 0, null, StatusEnum.OK.getValue(), new Date(), new Date());
                        addUserXLibraryList.add(userXLibraryEntity);
                    }
                    StudyRemindEntity studyRemindEntity = studyRemindService.getByCondition(companyId, libraryId, userId);
                    if (studyRemindEntity == null) {
                        addStudyRemind.add(new StudyRemindEntity(companyId, libraryId, userId, StatusEnum.OK.getValue(), new Date(), new Date()));
                    } else if (studyRemindEntity.getStatus() != null && studyRemindEntity.getStatus().equals(StatusEnum.DELETE.getValue())) {
                        updateStudyRemind.add(new StudyRemindEntity(studyRemindEntity.getId(), StatusEnum.OK.getValue(), new Date()));
                    }
                }
                if (addUserXLibraryList.size() > 0) {
                    Integer addUserXLibraryResult = userXLibraryService.batchInsertUserXLibrary(addUserXLibraryList);
                    logger.info(companyId+"同步了" + addUserXLibraryResult + "个用户题库!");
                } else {
                    logger.info("没有需要同步的用户题库!");
                }
                if (addStudyRemind.size() > 0) {
                    Integer result = studyRemindService.batchInsertStudyRemind(addStudyRemind);
                    logger.info(companyId+"学习提醒新增了" + result + "条数据!");
                }
                if (updateStudyRemind.size() > 0) {
                    Integer result = studyRemindService.batchUpdateStudyRemind(updateStudyRemind);
                    logger.info(companyId+"学习提醒更新了" + result + "条数据!");
                }
            }
            logger.info(corpId + "新增用户同步结束 >" + (System.currentTimeMillis() - begin) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
