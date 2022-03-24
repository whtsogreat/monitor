package com.mt.CenEAISiteRealtime.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mt.CenEAISiteRealtime.entity.CenEAISiteRealtimeEntity;

/**
 * 前置机实时信息表
 *
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
public interface CenEAISiteRealtimeService extends IService<CenEAISiteRealtimeEntity> {
    IPage<CenEAISiteRealtimeEntity> selectPage(Integer pageIndex, Integer pageSize, CenEAISiteRealtimeEntity cenEAISiteRealtime);

}

