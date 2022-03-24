package com.mt.CenEAISiteRealtime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mt.CenEAISiteRealtime.dao.CenEAISiteStartupHistoryDao;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteStartupHistoryEntity;
import com.mt.CenEAISiteRealtime.service.CenEAISiteStartupHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 前置机重启历史
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */

@Service("cenEAISiteStartupHistoryService")
public class CenEAISiteStartupHistoryServiceImpl extends ServiceImpl<CenEAISiteStartupHistoryDao, CenEAISiteStartupHistoryEntity> implements CenEAISiteStartupHistoryService {

    @Autowired
    private CenEAISiteStartupHistoryDao cenEAISiteStartupHistoryDao;
    @Override
    public IPage<CenEAISiteStartupHistoryEntity> selectPage(Integer pageNo, Integer pageSize, CenEAISiteStartupHistoryEntity cenEAISiteStartupHistory) {
        if (pageNo == null) pageNo = 1;
        if (pageSize == null) pageSize = 10;
        Page<CenEAISiteStartupHistoryEntity> page = new Page<>(pageNo, pageSize);
        QueryWrapper<CenEAISiteStartupHistoryEntity> cenEAISiteStartupHistoryEntityQueryWrapper = new QueryWrapper<>();
        if (cenEAISiteStartupHistory.getCenterId() != null && cenEAISiteStartupHistory.getCenterId() != "") {
            cenEAISiteStartupHistoryEntityQueryWrapper.eq("CenterId", cenEAISiteStartupHistory.getCenterId());
        }
        if (cenEAISiteStartupHistory.getSiteId() != null && cenEAISiteStartupHistory.getSiteId() != "") {
            cenEAISiteStartupHistoryEntityQueryWrapper.eq("SiteId", cenEAISiteStartupHistory.getSiteId());
        }
        IPage<CenEAISiteStartupHistoryEntity> cenEAISiteStartupHistoryIPage= cenEAISiteStartupHistoryDao.selectPage(page,cenEAISiteStartupHistoryEntityQueryWrapper);

        return cenEAISiteStartupHistoryIPage;
    }


}