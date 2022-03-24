package com.mt.CenEAISiteRealtime.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mt.CenEAISiteRealtime.dao.CenEAISiteStartupHistoryDao;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteStartupHistoryEntity;
import com.mt.CenEAISiteRealtime.service.CenEAISiteStartupHistoryService;
import com.mt.auth.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Arrays;


/**
 * 前置机重启历史
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@RestController
@RequestMapping("/cenEAISiteStartupHistory")
public class CenEAISiteStartupHistoryController {
    @Autowired
    private CenEAISiteStartupHistoryService cenEAISiteStartupHistoryService;
//    @Autowired
//    private  TokenUtils tokenUtils;//引入登录用户权限类
    @Autowired
    private CenEAISiteStartupHistoryDao cenEAISiteStartupHistoryDao;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(CenEAISiteStartupHistoryEntity cenEAISiteStartupHistory, Integer pageIndex, Integer pageSize){
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
//        HrUserEmp user = tokenUtils.getUserInfo();
//        if (user == null) {
//            return jsonResponse.setFailed(2,"暂无权限");
//        }
        IPage<CenEAISiteStartupHistoryEntity> page = cenEAISiteStartupHistoryService.selectPage(pageIndex,pageSize,cenEAISiteStartupHistory);

        jsonResponse.put("data", page);
        return jsonResponse.setSuccess("OK");
    }


    /**
     * 信息
     */
    @RequestMapping("/info")
    public JsonResponse info(String SiteId){
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
//        HrUserEmp user = tokenUtils.getUserInfo();
//        if (user == null) {
//            return jsonResponse.setFailed(2,"暂无权限");
//        }
		CenEAISiteStartupHistoryEntity cenEAISiteStartupHistory = cenEAISiteStartupHistoryDao.selectOne(
                Wrappers.<CenEAISiteStartupHistoryEntity>query()
                        .eq("SiteId",SiteId)
        );
        jsonResponse.put("cenEAISiteStartupHistory", cenEAISiteStartupHistory);
        return jsonResponse.setSuccess("OK");
    }

}
