package com.mt.demo2.controller;

import com.mt.auth.hr.HrUserEmp;
import com.mt.auth.utils.JsonResponse;
import com.mt.auth.utils.TokenUtils;
import com.mt.common.annotation.SysLog;
import com.mt.common.config.ServerConfig;
import com.mt.common.utils.AjaxResult;
import com.mt.common.utils.ProjectConfig;
import com.mt.common.utils.StringUtils;
import com.mt.common.utils.constant.Constants;
import com.mt.common.utils.file.FileUploadUtils;
import com.mt.common.utils.file.FileUtils;
import com.mt.demo2.service.RiskService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final @NonNull TokenUtils tokenUtils;


    @Autowired
    private ServerConfig serverConfig;


    @SysLog("这是一段测试用例")
    @GetMapping("/aaa/bbb/{name}")
    public String demoResource(@PathVariable("name") String name) {
        HrUserEmp user = tokenUtils.getUserInfo();

        return "Hello, " + name + ", current login user is:" + user.getLoginName();
    }

    /**
     * @Author: Caspar
     * @Date: 09:40 2021/7/8
     * @Description: 通用删除请求
     */
    @GetMapping("common/delete")
    public AjaxResult delete(String fileName,  HttpServletResponse response, HttpServletRequest request) {
        try {
            String filePath = ProjectConfig.getUploadPath() + fileName;
            FileUtils.deleteFile(filePath);
            AjaxResult ajax = AjaxResult.success();
            return ajax;
        } catch (Exception e) {
            System.out.println("删除文件失败");
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用下载请求
     *      * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = ProjectConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            /*if (delete)
            {
                FileUtils.deleteFile(filePath);
            }*/
        }
        catch (Exception e)
        {
            System.out.println("下载文件失败");
            System.out.println(e);
        }
    }


    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = ProjectConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = ProjectConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
        }
    }

    @Autowired
    private RiskService riskService;

    /**
     * 解析excel,数据入库
     *
     * @param file
     * @return
     */
    @RequestMapping("/parse")
    @Transactional
    @ResponseBody
    public JsonResponse parseExcelToDatabase(@RequestParam("file") MultipartFile file) {
        JsonResponse response = new JsonResponse();
        String fileName = file.getOriginalFilename();
        try {
            boolean result = riskService.excelParse(fileName, file);
            if (!result) {
                response.setFailed(2, "保存失败");
            }
            return response.setSuccess("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return response.setFailed(2, e.getMessage());
        }
    }


}
