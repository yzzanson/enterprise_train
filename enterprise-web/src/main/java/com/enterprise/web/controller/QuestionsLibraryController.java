package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.LoginUser;
import com.enterprise.base.common.PageEntity;
import com.enterprise.base.common.ResultJson;
import com.enterprise.base.entity.QuestionsLibraryEntity;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.SubjectEnum;
import com.enterprise.service.question.QuestionsLibraryService;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.util.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by anson on 18/3/26.
 */
@Controller
@RequestMapping("/questions_library")
public class QuestionsLibraryController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private QuestionsLibraryService questionsLibraryService;

    @Resource
    private QuestionsService questionsService;

    /**
     * 新增or修改题库
     */
    @RequestMapping(value = "/createOrUpdate.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createOrUpdateQuestionLibrary(QuestionsLibraryEntity questionsLibraryEntity) {
        //停用题库
        if (questionsLibraryEntity.getId() != null && (questionsLibraryEntity.getStatus() != null && questionsLibraryEntity.getStatus().equals(2))) {
            Integer subject = SubjectEnum.MODEL.getValue();//题库模板0
            if (questionsLibraryEntity.getSubject() == null) {
                QuestionsLibraryEntity questionsLibraryInDB = questionsLibraryService.getById(questionsLibraryEntity.getId());
                if (questionsLibraryInDB != null) {
                    subject = questionsLibraryInDB.getSubject();
                }
            } else {
                subject = questionsLibraryEntity.getSubject();
            }
            if (subject.equals(SubjectEnum.PUBLIC.getValue()) || subject.equals(SubjectEnum.ENTERPRISE.getValue())) {//官方 or 企业题库
                return ResultJson.errorResultJson("非题库模板不能停用!");
            }
            //非运营不能创建官方题库
        } else if (questionsLibraryEntity.getSubject() != null && questionsLibraryEntity.getSubject().equals(SubjectEnum.PUBLIC.getValue())) {//官方题库
            if (questionsLibraryEntity.getId() == null && questionsLibraryEntity.getDefaultFlag() == null) {
                questionsLibraryEntity.setDefaultFlag(0);
                questionsLibraryEntity.setUseCount(0);
                //设置为默认后的官方题库-自动将该题库同步给所有的用户
            }
        }
        if (questionsLibraryEntity.getStatus() == null) {
            questionsLibraryEntity.setStatus(StatusEnum.OK.getValue());
        }
        LoginUser loginUser = LoginUser.getUser();
        if (questionsLibraryEntity.getId() == null) {
            questionsLibraryEntity.setOperator(loginUser.getUserID());
        }
        if (questionsLibraryEntity.getCompanyId() == null) {
            questionsLibraryEntity.setCompanyId(loginUser.getCompanyID());
        }
        if (questionsLibraryEntity.getSubject() != null && questionsLibraryEntity.getSubject() == 0 && questionsLibraryEntity.getId() == null) {
            questionsLibraryEntity.setStatus(StatusEnum.HOLD_ON.getValue());
        }
        if (questionsLibraryEntity.getId() == null && questionsLibraryEntity.getSubject().equals(SubjectEnum.PUBLIC.getValue())) {
            questionsLibraryEntity.setDefaultFlag(0);
        }
        if (StringUtils.isNotEmpty(questionsLibraryEntity.getTitle())) {
            if (questionsLibraryEntity.getTitle().length() > 5) {
                return ResultJson.errorResultJson("不合理的头衔长度!");
            }
        }
        Integer result = questionsLibraryService.createOrUpdateQuestionLibrary(questionsLibraryEntity);
        if (result > 0) {
            return ResultJson.succResultJson(questionsLibraryEntity);
        } else
            return ResultJson.errorResultJson("questionLibrary_saveOrUpdate失败");
    }


    /**
     * 新增or修改题库
     */
    @RequestMapping(value = "/checkLibraryCount.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject checkLibraryCount() {
        return questionsLibraryService.checkLibraryCount();
    }

    /**
     * 使用题库
     */
    @RequestMapping(value = "/useLibrary.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject useLibrary(String libraryIds) {
        LoginUser loginUser = LoginUser.getUser();
        AssertUtil.notNull(loginUser.getCompanyID(), "公司不能为空");
        AssertUtil.notNull(loginUser.getUserID(), "用户不能为空");
        JSONObject jsonObject = questionsLibraryService.useLibrary(libraryIds);
        return jsonObject;
    }

    /**
     * 批量删除题库
     */
    @RequestMapping(value = "/batchDelete.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject batchDelete(String libraryIds) {
        JSONObject jsonObject = questionsLibraryService.batchDelete(libraryIds);
        return jsonObject;
    }

    /**
     * 批量删除题库,官方题库
     */
    @RequestMapping(value = "/batchDelete2.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject batchDelete2(String libraryIds) {
        JSONObject jsonObject = questionsLibraryService.batchDelete2(libraryIds);
        return jsonObject;
    }

    /**
     * 批量删除题库

     @RequestMapping(value="/batchDelete2.json",method= RequestMethod.POST)
     @ResponseBody public JSONObject batchDelete2(String libraryList){
     List<SimpleQuestionLibraryVO> simpleQuestionLibraryList = JSONUtil.getListObj(libraryList, SimpleQuestionLibraryVO.class);
     JSONObject jsonObject = questionsLibraryService.batchDelete2(simpleQuestionLibraryList);
     return jsonObject;
     }
      * */

    /**
     * 企业信息-获取标签列表
     */
    @RequestMapping("/getLabelList.json")
    @ResponseBody
    public JSONObject getLabelList(Integer subject, PageEntity pageEntity) {
        try {
            JSONObject labelJson = questionsLibraryService.getLabelList(subject, pageEntity);
            return labelJson;
        } catch (Exception e) {
            logger.error("questions_library-labelJson异常:" + e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }


    /**
     * 官方题库-分类为钉钉的为钉钉题库
     * 企业信息-获取题库列表
     */
    @RequestMapping("/getLibraryList.json")
    @ResponseBody
    public JSONObject getLibraryList(QuestionsLibraryEntity questionsLibraryEntity, PageEntity pageEntity) {
        try {
            JSONObject questionFeedBackJson = questionsLibraryService.findQuestionsLibrary(questionsLibraryEntity, pageEntity);
            return questionFeedBackJson;
        } catch (Exception e) {
            logger.error("questions_library-getLibraryList异常:" + e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }

    /**
     * 官方题库-分类为钉钉的为钉钉题库
     * 企业信息-获取题库列表
     */
    @RequestMapping("/getLibraryDetail.json")
    @ResponseBody
    public JSONObject getLibraryDetail(Integer id) {
        try {
            JSONObject questionLibraryDetailkJson = questionsLibraryService.getLibraryDetail(id);
            return questionLibraryDetailkJson;
        } catch (Exception e) {
            logger.error("questions_library-getLibraryDetail异常:" + e.getMessage());
        }
        return ResultJson.succResultJson("SUCCESS", null);
    }


    /**
     * 查看题库下题目详情列表
     */
    @RequestMapping(value = "/getQuestionDetailList.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getQuestionDetailList(Integer libraryId, PageEntity pageEntity) {
        JSONObject questionListJson = questionsService.getQuestionDetailList(libraryId, pageEntity);
        return questionListJson;
    }

    /**
     * 导入题库--共用
     * 默认题库是否要加入公共题库,是否计算此题库的使用次数
     * file文件名
     * label标签
     */
    @RequestMapping(value = "/importQuestions.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject importQuestions(HttpServletRequest request) {
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
        MultipartFile file = multipartRequest.getFileMap().get("file");
        Integer libraryId = new Integer(multipartRequest.getParameter("libraryId").trim());
        AssertUtil.isTrue(libraryId != null, "题库id不能为空");
        String temppath = request.getSession().getServletContext().getRealPath("upload");
        String fileName1 = file.getOriginalFilename();
        if(!(fileName1.endsWith("xls") || fileName1.endsWith("xlsx"))){
            return ResultJson.errorResultJson("不合法的文件!");
        }
        File targetFile = new File(temppath, fileName1);

        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            //将文件保存到服务器
            file.transferTo(targetFile);
            return questionsService.importQuestions(targetFile, libraryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultJson.errorResultJson("");
    }


    /**
     * 导出题模板
     */
    @RequestMapping(value = "/exportModel.json", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportModel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String storeName = "橘猫企训题目模板.xlsx";
            request.setCharacterEncoding("UTF-8");
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            //获取项目根目录
            String ctxPath = request.getSession().getServletContext()
                    .getRealPath("");
            //获取下载文件路径
            String downLoadPath = ctxPath + "WEB-INF/classes/download/" + storeName;

            //获取文件的长度
            long fileLength = new File(downLoadPath).length();

            //设置文件输出类型
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(storeName.getBytes("utf-8"), "ISO8859-1"));
            //设置输出长度
            response.setHeader("Content-Length", String.valueOf(fileLength));
            //获取输入流
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            //输出流
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            //关闭流
            bis.close();
            bos.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出题库内所有题目
     */
    @RequestMapping(value = "/exportQuestionLibrary.json", method = RequestMethod.GET)
    public void exportQuestionLibrary(HttpServletResponse response, Integer libraryId) {
        questionsLibraryService.exportQuestionLibrary(response, libraryId);
    }


    /**
     * 导入题库--自己用
     * 默认题库是否要加入公共题库,是否计算此题库的使用次数
     */
    @RequestMapping(value = "/importQuestionLibrary.json")
    @ResponseBody
    public JSONObject importQuestionLibrary(Integer libraryId, String filePath, String sheetName, Integer startRow, String label) {
        JSONObject questionListJson = questionsLibraryService.importQuestionLibrary(libraryId, filePath, sheetName, startRow, label);
        return questionListJson;
    }

    @RequestMapping(value = "/getArrangerdCompany.json")
    @ResponseBody
    public JSONObject getArrangerdCompany(String search, Integer libraryId, PageEntity pageEntity) {
        JSONObject questionListJson = questionsLibraryService.getArrangerdCompany(search, libraryId, pageEntity);
        return questionListJson;
    }


    @RequestMapping(value = "/arrangeOfficialLibrary.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject arrangeOfficialLibrary(String companyIds, Integer libraryId) {
        JSONObject questionListJson = questionsLibraryService.arrangeOfficialLibrary(companyIds, libraryId);
        return questionListJson;
    }

    /**
     * 获取题库完成度
     * */
    @RequestMapping(value = "/getLibraryCompletion.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLibraryCompletion(Integer libraryId) {
        AssertUtil.notNull(libraryId, "题库id不能为空");
        JSONObject questionListJson = questionsLibraryService.getLibraryCompletion(libraryId);
        return questionListJson;
    }

    /**
     * 题库完成or未完成人列表
     * */
    @RequestMapping(value = "/getLibraryCompletionDetail.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLibraryCompletionDetail(Integer libraryId,Integer type,PageEntity pageEntity) {
        AssertUtil.notNull(libraryId, "题库id不能为空");
        AssertUtil.notNull(type, "type不能为空");
        JSONObject questionListJson = questionsLibraryService.getLibraryCompletionDetail(libraryId, type, pageEntity);
        return questionListJson;
    }

    /**
     * 题库正确率列表
     * */
    @RequestMapping(value = "/getLibraryAccuracy.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLibraryAccuracy(Integer libraryId,PageEntity pageEntity) {
        AssertUtil.notNull(libraryId, "题库id不能为空");
        JSONObject questionListJson = questionsLibraryService.getLibraryAccuracy(libraryId, pageEntity);
        return questionListJson;
    }

    /**
     * 学员完成度导出
     * */
    @RequestMapping(value = "/exportLibraryCompletionDetail.json", method = RequestMethod.GET)
    @ResponseBody
    public void exportLibraryCompletionDetail(Integer libraryId) {
        AssertUtil.notNull(libraryId, "题库id不能为空");
        questionsLibraryService.exportLibraryCompletionDetail(libraryId);
    }


    /**
     * 题库正确率导出
     * */
    @RequestMapping(value = "/exportLibraryAccuracy.json", method = RequestMethod.GET)
    @ResponseBody
    public void exportLibraryAccuracy(Integer libraryId) {
        AssertUtil.notNull(libraryId, "题库id不能为空");
        questionsLibraryService.exportLibraryAccuracy(libraryId);
    }


    public static void main(String[] args) {
        Integer libraryId = 2;
        AssertUtil.isTrue(libraryId != null, "题库id不能为空");
    }


}
