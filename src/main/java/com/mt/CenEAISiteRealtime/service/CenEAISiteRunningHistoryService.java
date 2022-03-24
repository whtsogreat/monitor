package com.mt.CenEAISiteRealtime.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteRunningHistoryEntity;

/**
 * 前置机运行状态历史
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
public interface CenEAISiteRunningHistoryService extends IService<CenEAISiteRunningHistoryEntity> {
    IPage<CenEAISiteRunningHistoryEntity> selectPage(Integer pageIndex, Integer pageSize, CenEAISiteRunningHistoryEntity cenEAISiteRunningHistory);

}

