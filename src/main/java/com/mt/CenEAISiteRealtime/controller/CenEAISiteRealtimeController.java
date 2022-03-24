package com.mt.CenEAISiteRealtime.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mt.CenEAISiteRealtime.dao.*;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteRealtimeEntity;
import com.mt.CenEAISiteRealtime.entity.DicCenterEntity;
import com.mt.CenEAISiteRealtime.service.CenEAISiteRealtimeService;
import com.mt.auth.utils.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Arrays;


/**
 * 前置机实时信息表
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@RestController
@RequestMapping("/cenEAISiteRealtime")
public class CenEAISiteRealtimeController {
    @Autowired
    private CenEAISiteRealtimeService cenEAISiteRealtimeService;
//    @Autowired
//    private  TokenUtils tokenUtils;//引入登录用户权限类
    @Autowired
    private CenEAISiteRealtimeDao cenEAISiteRealtimeDao;
    @Autowired
    private DicCenterDao dicCenterDao;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(CenEAISiteRealtimeEntity cenEAISiteRealtime, Integer pageIndex, Integer pageSize){
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
//        HrUserEmp user = tokenUtils.getUserInfo();
//        if (user == null) {
//            return jsonResponse.setFailed(2,"暂无权限");
//        }
        IPage<CenEAISiteRealtimeEntity> page = cenEAISiteRealtimeService.selectPage(pageIndex,pageSize,cenEAISiteRealtime);

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
		CenEAISiteRealtimeEntity cenEAISiteRealtime = cenEAISiteRealtimeDao.selectOne(
                Wrappers.<CenEAISiteRealtimeEntity>query()
                        .eq("SiteId",SiteId)
        );
        DicCenterEntity dicCenterEntity = dicCenterDao.selectOne(
                Wrappers.<DicCenterEntity>query()
                        .eq("code",cenEAISiteRealtime.getCenterId())
        );
        cenEAISiteRealtime.setCenterChineseName(dicCenterEntity.getRemark());
        jsonResponse.put("cenEAISiteRealtime", cenEAISiteRealtime);
        return jsonResponse.setSuccess("OK");
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public JsonResponse save(CenEAISiteRealtimeEntity cenEAISiteRealtime){
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
//        HrUserEmp user = tokenUtils.getUserInfo();
//        if (user == null) {
//            return jsonResponse.setFailed(2,"暂无权限");
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cenEAISiteRealtimeService.save(cenEAISiteRealtime);

        return jsonResponse.setSuccess("OK");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResponse update(CenEAISiteRealtimeEntity cenEAISiteRealtime){
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
//        HrUserEmp user = tokenUtils.getUserInfo();
//        if (user == null) {
//            return jsonResponse.setFailed(2,"暂无权限");
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cenEAISiteRealtimeService.updateById(cenEAISiteRealtime);

        return jsonResponse.setSuccess("OK");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(String[] codes){
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
//        HrUserEmp user = tokenUtils.getUserInfo();
//        if (user == null) {
//            return jsonResponse.setFailed(2,"暂无权限");
//        }
		cenEAISiteRealtimeService.removeByIds(Arrays.asList(codes));

        return jsonResponse.setSuccess("OK");
    }

}
