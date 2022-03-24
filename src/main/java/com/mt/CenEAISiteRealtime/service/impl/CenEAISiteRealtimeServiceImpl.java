package com.mt.CenEAISiteRealtime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mt.CenEAISiteRealtime.dao.CenEAIHeartBeatRealtimeDao;
import com.mt.CenEAISiteRealtime.dao.CenEAISiteRealtimeDao;
import com.mt.CenEAISiteRealtime.dao.DicCenterDao;
import com.mt.CenEAISiteRealtime.entity.CenEAIHeartBeatRealtimeEntity;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteRealtimeEntity;
import com.mt.CenEAISiteRealtime.entity.DicCenterEntity;
import com.mt.CenEAISiteRealtime.service.CenEAISiteRealtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 前置机实时信息表
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */

@Service("cenEAISiteRealtimeService")
public class CenEAISiteRealtimeServiceImpl extends ServiceImpl<CenEAISiteRealtimeDao, CenEAISiteRealtimeEntity> implements CenEAISiteRealtimeService {

    @Autowired
    private CenEAISiteRealtimeDao cenEAISiteRealtimeDao;
    @Autowired
    private DicCenterDao dicCenterDao;
    @Autowired
    private CenEAIHeartBeatRealtimeDao cenEAIHeartBeatRealtimeDao;
    @Override
    public IPage<CenEAISiteRealtimeEntity> selectPage(Integer pageNo, Integer pageSize, CenEAISiteRealtimeEntity cenEAISiteRealtime) {
        if (pageNo == null) pageNo = 1;
        if (pageSize == null) pageSize = 10;
        Page<CenEAISiteRealtimeEntity> page = new Page<>(pageNo, pageSize);
        QueryWrapper<CenEAISiteRealtimeEntity> cenEAISiteRealtimeEntityQueryWrapper = new QueryWrapper<>();
        if (cenEAISiteRealtime.getCenterId() != null && cenEAISiteRealtime.getCenterId() != "") {
            cenEAISiteRealtimeEntityQueryWrapper.eq("CenterId", cenEAISiteRealtime.getCenterId());
        }
        if (cenEAISiteRealtime.getSiteId() != null && cenEAISiteRealtime.getSiteId() != "") {
            cenEAISiteRealtimeEntityQueryWrapper.eq("SiteId", cenEAISiteRealtime.getSiteId());
        }
        if (cenEAISiteRealtime.getSiteStatus() != null && cenEAISiteRealtime.getSiteStatus() != "") {
            cenEAISiteRealtimeEntityQueryWrapper.eq("SiteStatus", cenEAISiteRealtime.getSiteStatus());
        }
        if (cenEAISiteRealtime.getRunningStatus() != null && cenEAISiteRealtime.getRunningStatus() != "") {
            cenEAISiteRealtimeEntityQueryWrapper.eq("RunningStatus", cenEAISiteRealtime.getRunningStatus());
        }
        if (cenEAISiteRealtime.getIP_Address() != null && cenEAISiteRealtime.getIP_Address() != "") {
            cenEAISiteRealtimeEntityQueryWrapper.eq("IP_Address", cenEAISiteRealtime.getIP_Address());
        }
        IPage<CenEAISiteRealtimeEntity> cenEAISiteRealtimeIPage= cenEAISiteRealtimeDao.selectPage(page,cenEAISiteRealtimeEntityQueryWrapper);

        for(int i = 0; i < page.getRecords().size(); i++){
            if(page.getRecords().get(i) != null){
                DicCenterEntity dicCenterEntity = dicCenterDao.selectOne(
                        Wrappers.<DicCenterEntity>query()
                                .eq("code",page.getRecords().get(i).getCenterId())
                );
                page.getRecords().get(i).setCenterChineseName(dicCenterEntity.getRemark());
            }
            Integer num = cenEAIHeartBeatRealtimeDao.selectCount(
                    Wrappers.<CenEAIHeartBeatRealtimeEntity>query()
                            .eq("SiteId",page.getRecords().get(i).getSiteId())
            );
            page.getRecords().get(i).setNum(num);
        }

        return cenEAISiteRealtimeIPage;
    }


}