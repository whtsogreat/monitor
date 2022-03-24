package com.mt.CenEAISiteRealtime.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteStartupHistoryEntity;

/**
 * 前置机重启历史
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
public interface CenEAISiteStartupHistoryService extends IService<CenEAISiteStartupHistoryEntity> {
    IPage<CenEAISiteStartupHistoryEntity> selectPage(Integer pageIndex, Integer pageSize, CenEAISiteStartupHistoryEntity cenEAISiteStartupHistory);

}

