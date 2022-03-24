package com.mt.CenEAISiteRealtime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mt.CenEAISiteRealtime.dao.CenEAISiteRunningHistoryDao;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteRunningHistoryEntity;
import com.mt.CenEAISiteRealtime.service.CenEAISiteRunningHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 前置机运行状态历史
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */

@Service("cenEAISiteRunningHistoryService")
public class CenEAISiteRunningHistoryServiceImpl extends ServiceImpl<CenEAISiteRunningHistoryDao, CenEAISiteRunningHistoryEntity> implements CenEAISiteRunningHistoryService {

    @Autowired
    private CenEAISiteRunningHistoryDao cenEAISiteRunningHistoryDao;
    @Override
    public IPage<CenEAISiteRunningHistoryEntity> selectPage(Integer pageNo, Integer pageSize, CenEAISiteRunningHistoryEntity cenEAISiteRunningHistory) {
        if (pageNo == null) pageNo = 1;
        if (pageSize == null) pageSize = 10;
        Page<CenEAISiteRunningHistoryEntity> page = new Page<>(pageNo, pageSize);
        QueryWrapper<CenEAISiteRunningHistoryEntity> cenEAISiteRunningHistoryEntityQueryWrapper = new QueryWrapper<>();
        if (cenEAISiteRunningHistory.getCenterId() != null && cenEAISiteRunningHistory.getCenterId() != "") {
            cenEAISiteRunningHistoryEntityQueryWrapper.eq("CenterId", cenEAISiteRunningHistory.getCenterId());
        }
        if (cenEAISiteRunningHistory.getSiteId() != null && cenEAISiteRunningHistory.getSiteId() != "") {
            cenEAISiteRunningHistoryEntityQueryWrapper.eq("SiteId", cenEAISiteRunningHistory.getSiteId());
        }
        IPage<CenEAISiteRunningHistoryEntity> cenEAISiteRunningHistoryEntityIPage= cenEAISiteRunningHistoryDao.selectPage(page,cenEAISiteRunningHistoryEntityQueryWrapper);

        return cenEAISiteRunningHistoryEntityIPage;
    }


}