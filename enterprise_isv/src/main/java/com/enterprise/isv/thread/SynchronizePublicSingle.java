package com.enterprise.isv.thread;

import com.enterprise.base.common.SpringContextHolder;
import com.enterprise.base.entity.CompanyXLibraryEntity;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.QuestionsLibraryEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.service.company.CompanyXLibraryService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.question.QuestionsLibraryService;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * @Description 单个企业同步所有的官方题库
 * @Author zezhouyang
 * @Date 18/6/11 下午3:23
 */
public class SynchronizePublicSingle implements Runnable {

    private String corpId;

    public SynchronizePublicSingle(String corpId) {
        this.corpId = corpId;
    }

    private CompanyXLibraryService companyXLibraryService = SpringContextHolder.getBean(CompanyXLibraryService.class);
    private IsvTicketsService isvTicketsService = SpringContextHolder.getBean(IsvTicketsService.class);
    private QuestionsLibraryService questionsLibraryService = SpringContextHolder.getBean(QuestionsLibraryService.class);

    @Override
    public void run() {
        //遍历当前公司将其所有员工获取该题库
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
        Integer companyId = isvTicketsEntity.getCompanyId();
        //分配给所有企业的 和 分配给该企业的
        List<CompanyXLibraryEntity> defaultLibraryList = companyXLibraryService.getCompanyLibraryList(0, StatusEnum.OK.getValue());
        List<CompanyXLibraryEntity> addCompanyLibraryList = new ArrayList<>();
        List<CompanyXLibraryEntity> updateCompanyLibraryList = new ArrayList<>();
        Set<Integer> addQuestionLibrary = new HashSet<>();
        if (CollectionUtils.isNotEmpty(defaultLibraryList)) {
            for (int i = 0; i < defaultLibraryList.size(); i++) {
                Integer libraryId = defaultLibraryList.get(i).getLibraryId();
                CompanyXLibraryEntity companyLibraryInDB = companyXLibraryService.findCompanyXLibrary(companyId, libraryId);
                if (companyLibraryInDB == null) {
                    addQuestionLibrary.add(libraryId);
                    CompanyXLibraryEntity addCompanyXLibrary = new CompanyXLibraryEntity(companyId, libraryId, 0, StatusEnum.OK.getValue(), new Date(), new Date());
                    addCompanyLibraryList.add(addCompanyXLibrary);
                } else if (companyLibraryInDB != null && companyLibraryInDB.getStatus().equals(0)) {
                    addQuestionLibrary.add(libraryId);
                    CompanyXLibraryEntity updateCompanyXLibrary = new CompanyXLibraryEntity(companyLibraryInDB.getId(), StatusEnum.OK.getValue(), new Date());
                    updateCompanyLibraryList.add(updateCompanyXLibrary);
                }
            }
        }

        //增加默认官方题库default_status = 1 ,并且没有全部分配的官方题库(之前已经分配过)
        List<QuestionsLibraryEntity> defaultQuestoinLibraryList = questionsLibraryService.getDefaultQuestionLibraryNoAssigned();
        if(CollectionUtils.isNotEmpty(defaultQuestoinLibraryList)){
            for (int i = 0; i < defaultQuestoinLibraryList.size(); i++) {
                Integer questionLibraryId = defaultQuestoinLibraryList.get(i).getId();
                if(!addQuestionLibrary.contains(questionLibraryId)){
                    CompanyXLibraryEntity companyLibraryInDB = companyXLibraryService.findCompanyXLibrary(companyId, questionLibraryId);
                    if (companyLibraryInDB == null) {
                        addQuestionLibrary.add(questionLibraryId);
                        CompanyXLibraryEntity addCompanyXLibrary = new CompanyXLibraryEntity(companyId, questionLibraryId, 0, StatusEnum.OK.getValue(), new Date(), new Date());
                        addCompanyLibraryList.add(addCompanyXLibrary);
                    } else if (companyLibraryInDB != null && companyLibraryInDB.getStatus().equals(0)) {
                        addQuestionLibrary.add(questionLibraryId);
                        CompanyXLibraryEntity updateCompanyXLibrary = new CompanyXLibraryEntity(companyLibraryInDB.getId(), StatusEnum.OK.getValue(), new Date());
                        updateCompanyLibraryList.add(updateCompanyXLibrary);
                    }

                }
            }
        }

        if (CollectionUtils.isNotEmpty(addCompanyLibraryList)) {
            companyXLibraryService.batchInsert(addCompanyLibraryList);
        }
        if (CollectionUtils.isNotEmpty(updateCompanyLibraryList)) {
            companyXLibraryService.batchupdate(updateCompanyLibraryList);
        }

    }


}
