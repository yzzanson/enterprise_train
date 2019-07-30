package com.enterprise.service.question;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.entity.QuestionsLibraryEntity;
import com.enterprise.util.excel.QuestionExcelDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description UserService
 * @Author anson
 * @Date 2018/3/26 下午14:47
 */
public interface QuestionsLibraryService {

    Integer createOrUpdateQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity);

    JSONObject findQuestionsLibrary(QuestionsLibraryEntity questionsLibraryEntity,PageEntity pageEntity);

    JSONObject getLibraryDetail(Integer id);

    JSONObject useLibrary(String libraryIds);

    /**
     * 继承其他的题库
     * */
    Integer inheritQuestionLibrary(List<QuestionsLibraryEntity> addDefaultQuestionLibrary);

    JSONObject batchDelete(String libraryIds);
    JSONObject batchDelete2(String libraryIds);

    JSONObject getLabelList(Integer subject,PageEntity pageEntity);

    /**
     * 导入默认的题库
     * */
    JSONObject importQuestionLibrary(Integer libraryId,String filePath, String sheetName,Integer startRow,String label);

    /**
     * 根据条件查询
     * */
    List<QuestionsLibraryEntity> getQuestionLibrarys(QuestionsLibraryEntity questionsLibraryEntity);

    QuestionsLibraryEntity getDefaultQuestionLibrary(Integer companyId,Integer parentId,Integer isDefault);

    /**
     * 获取除了已经全部分配企业的默认题库
     * */
   List<QuestionsLibraryEntity> getDefaultQuestionLibraryNoAssigned();

    QuestionsLibraryEntity getById(Integer id);

    /**
     * 导出某个题库的所有题目
     * */
    void exportQuestionLibrary(HttpServletResponse reponse,Integer libraryId);

    /**
     * 将list中的labl导入到库中
     * */
    void batchInsertQuestionLabel(Integer companyId,Integer libraryId, List<QuestionExcelDTO> resultList);

    /**
     * 增加题库的使用次数
     * */
    void addLibraryUseCount(Integer id,Integer addCount);

    /**
     * 获取所有企业列表,标注已经被分配该题库的企业
     * */
    JSONObject getArrangerdCompany(String search,Integer libraryId,PageEntity pageEntity);

    /**
     * 为企业分配官方题库
     * */
    JSONObject arrangeOfficialLibrary(String companyIds,Integer libraryId);

    /**
     * 获取题库完成度
     * */
    JSONObject getLibraryCompletion(Integer libraryId);

    /**
     * 题库完成or未完成人列表
     * */
    JSONObject getLibraryCompletionDetail(Integer libraryId,Integer type,PageEntity pageEntity);

    /**
     * 题库正确率列表
     * */
    JSONObject getLibraryAccuracy(Integer libraryId,PageEntity pageEntity);

    /**
     * 学员完成度导出
     * */
    void exportLibraryCompletionDetail(Integer libraryId);


    /**
     * 题库正确率导出
     * */
    void exportLibraryAccuracy(Integer libraryId);

    /**
     * 判断题库是否超过限制
     * */
    JSONObject checkLibraryCount();

}
