package com.mt.CenEAISiteRealtime.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mt.CenEAISiteRealtime.dao.CenEAISiteRunningHistoryDao;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteRunningHistoryEntity;
import com.mt.CenEAISiteRealtime.service.CenEAISiteRunningHistoryService;
import com.mt.auth.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 前置机运行状态历史
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@RestController
@RequestMapping("/cenEAISiteRunningHistory")
public class CenEAISiteRunningHistoryController {
    @Autowired
    private CenEAISiteRunningHistoryService cenEAISiteRunningHistoryService;
//    @Autowired
//    private  TokenUtils tokenUtils;//引入登录用户权限类
    @Autowired
    private CenEAISiteRunningHistoryDao cenEAISiteRunningHistoryDao;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(CenEAISiteRunningHistoryEntity cenEAISiteRunningHistory, Integer pageIndex, Integer pageSize){
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
//        HrUserEmp user = tokenUtils.getUserInfo();
//        if (user == null) {
//            return jsonResponse.setFailed(2,"暂无权限");
//        }
        IPage<CenEAISiteRunningHistoryEntity> page = cenEAISiteRunningHistoryService.selectPage(pageIndex,pageSize,cenEAISiteRunningHistory);

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
		CenEAISiteRunningHistoryEntity cenEAISiteRunningHistory = cenEAISiteRunningHistoryDao.selectOne(
                Wrappers.<CenEAISiteRunningHistoryEntity>query()
                        .eq("SiteId",SiteId)
        );
        jsonResponse.put("cenEAISiteRunningHistory", cenEAISiteRunningHistory);
        return jsonResponse.setSuccess("OK");
    }

}
