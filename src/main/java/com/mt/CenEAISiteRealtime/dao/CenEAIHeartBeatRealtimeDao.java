package com.mt.CenEAISiteRealtime.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mt.CenEAISiteRealtime.entity.CenEAIHeartBeatRealtimeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 前置机监控实时心跳
 * 
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@Mapper
public interface CenEAIHeartBeatRealtimeDao extends BaseMapper<CenEAIHeartBeatRealtimeEntity> {
	
}
