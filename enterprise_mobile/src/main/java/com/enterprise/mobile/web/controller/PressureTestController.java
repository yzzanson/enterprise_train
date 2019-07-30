package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.mobile.web.exception.OverloadException;
import com.enterprise.service.paperBall.PaperBallService;
import com.enterprise.service.petFood.PetFoodService;
import com.enterprise.service.question.QuestionsService;
import com.enterprise.service.question.UserXLibraryService;
import com.enterprise.service.question.UserXQuestionsService;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/20 下午1:47
 */
@RequestMapping("/pressure")
@Controller
public class PressureTestController {

    @Resource
    private UserXLibraryService userXLibraryService;

    @Resource
    private QuestionsService questionsService;

    @Resource
    private UserXQuestionsService userXQuestionsService;

    @Resource
    private PaperBallService paperBallService;

    @Resource
    private PetFoodService petFoodService;

    //1秒允许调用50次
//    RateLimiter rateLimiter = RateLimiter.create(50, 1, TimeUnit.SECONDS);
    RateLimiter rateLimiter = RateLimiter.create(50);

    RateLimiter rateLimiter2 = RateLimiter.create(100);

    //1秒允许调用50次
//    RateLimiter rateLimiter2 = RateLimiter.create(5, 1, TimeUnit.SECONDS);
    RateLimiter rateLimiter3 = RateLimiter.create(15);

    RateLimiter rateLimiter4 = RateLimiter.create(30);

    /**
     * 查看用户题库学习进度
     */
    @RequestMapping(value = "/getUserStudyProcess.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserStudyProcess(Integer companyId, Integer userId) throws OverloadException {
//        if (!rateLimiter.tryAcquire()) {
//            throw new OverloadException("超过负载了");
//        }
//        return userXLibraryService.getUserStudyProcess(userId, companyId);
        return null;
    }

    /**
     * 获取下一题
     */
    @RequestMapping(value = "/getNextQuestion.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNextQuestion(@RequestParam("libraryId") Integer libraryId, @RequestParam("companyId") Integer companyId, @RequestParam("userId") Integer userId) throws OverloadException {
        if (!rateLimiter.tryAcquire()) {
            throw new OverloadException("超过负载了");
        }
        return questionsService.getNextQuestionTest(libraryId, companyId, userId);
    }


    /**
     * 获取下一题
     */
    @RequestMapping(value = "/getNextQuestion2.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNextQuestion2(@RequestParam("libraryId") Integer libraryId, @RequestParam("companyId") Integer companyId, @RequestParam("userId") Integer userId) throws OverloadException {
        if (!rateLimiter2.tryAcquire()) {
            throw new OverloadException("超过负载了");
        }
        return questionsService.getNextQuestionTest(libraryId, companyId, userId);
    }


    /**
     * 获取下一题
     */
    @RequestMapping(value = "/getNextQuestion3.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNextQuestion3(@RequestParam("libraryId") Integer libraryId, @RequestParam("companyId") Integer companyId, @RequestParam("userId") Integer userId) throws OverloadException {
        if (!rateLimiter.tryAcquire()) {
            throw new OverloadException("超过负载了");
        }
        return questionsService.getNextQuestionTest(libraryId, companyId, userId);
    }

    /**
     * 答题
     */
    @RequestMapping(value = "/answer.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject answer(@RequestParam("companyId") Integer companyId, @RequestParam("userId") Integer userId, @RequestParam("questionId") Integer questionId, @RequestParam("answer") String answer, @RequestParam("type") Integer type) throws OverloadException {
        if (!rateLimiter.tryAcquire()) {
            throw new OverloadException("超过负载了");
        }
        return userXQuestionsService.answerTest(companyId, userId, questionId, answer, type);

    }

    /**
     * 答题
     */
    @RequestMapping(value = "/answer2.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject answer2(@RequestParam("companyId") Integer companyId, @RequestParam("userId") Integer userId, @RequestParam("questionId") Integer questionId, @RequestParam("answer") String answer, @RequestParam("type") Integer type) throws OverloadException {
//        if (!rateLimiter2.tryAcquire()) {
//            throw new OverloadException("超过负载了");
//        }
        return userXQuestionsService.answerTest(companyId, userId, questionId, answer, type);

    }

    /**
     * 清除纸团
     */
    @RequestMapping(value = "/cleanBall.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject cleanBall(@RequestParam("companyId") Integer companyId, @RequestParam("userId") Integer userId, @RequestParam("ballId") Integer ballId) throws OverloadException {
//        if (!rateLimiter3.tryAcquire()) {
//            throw new OverloadException("超过负载了");
//        }
        return paperBallService.cleanBallTest(companyId, userId, ballId);
    }

    /**
     * 喂食
     */
    @RequestMapping(value = "/feedPet.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject feedPet(Integer companyId, Integer userId, Integer otherUserId) throws OverloadException {
//        if (!rateLimiter3.tryAcquire()) {
//            throw new OverloadException("超过负载了");
//        }
        return petFoodService.feedOtherPetTest(companyId, userId, otherUserId);
    }


}
