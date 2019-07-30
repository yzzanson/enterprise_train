package com.enterprise.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.BaseController;
import com.enterprise.base.common.GlobalConstant;
import com.enterprise.base.common.ResultJson;
import com.enterprise.util.image.ImageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/7/16 下午3:57
 */
@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

    private final String waterMarkPic = GlobalConstant.getRootPath()+GlobalConstant.WATERMARK_FILEPATH;

    @RequestMapping(value = "/uploadPic.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadPic(HttpServletRequest request) {
        String path = null;// 文件路径
        try {
            MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
            MultipartFile file = multipartRequest.getFileMap().get("file");

            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            System.out.println("上传的文件原名称:" + fileName);
            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase()) || "JPEG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
//                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
                    // 设置存放图片文件的路径
                    String realPath = GlobalConstant.getPicFilePath();
                    path = realPath +/*System.getProperty("file.separator")+*/trueFileName;
                    System.out.println("存放图片文件的路径:" + path);
                    // 转存文件到指定的路径
                    file.transferTo(new File(path));
                    System.out.println("文件成功上传到指定目录下");
                    ImageUtil.addWaterMark(path, waterMarkPic, 0, 0, 0);
                    String finalPath = GlobalConstant.getGateWay2()+"/static/neixun/"+trueFileName;
                    return ResultJson.succResultJson(finalPath);
                } else {
                    System.out.println("不是我们想要的文件类型,请按要求重新上传");
                    return null;
                }
            } else {
                System.out.println("文件类型为空");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultJson.errorResultJson("上传图片失败");
    }



    @RequestMapping(value = "/uploadPic2.json", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> uploadPic2(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String path = null;// 文件路径
        try {
            MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
            MultipartFile file = multipartRequest.getFileMap().get("file");

            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            System.out.println("上传的文件原名称:" + fileName);
            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase()) || "JPEG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
//                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
                    // 设置存放图片文件的路径
                    String realPath = GlobalConstant.getPicFilePath();
                    path = realPath +/*System.getProperty("file.separator")+*/trueFileName;
                    System.out.println("存放图片文件的路径:" + path);
                    // 转存文件到指定的路径
                    file.transferTo(new File(path));
                    System.out.println(path);
                    System.out.println("文件成功上传到指定目录下");
                    ImageUtil.addWaterMark(path, waterMarkPic, 0, 0, 0);
                    String finalPath = GlobalConstant.getGateWay() +"/static/neixun/"+trueFileName;
                    map.put("link", finalPath);

                    return map;
                } else {
                    System.out.println("不是我们想要的文件类型,请按要求重新上传");
                    return null;
                }
            } else {
                System.out.println("文件类型为空");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

}
