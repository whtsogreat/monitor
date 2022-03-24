package com.mt.CenEAISiteRealtime.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 前置机监控实时心跳表
 * 
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@Data
@TableName("CenEAI_HeartBeat_Realtime")
public class CenEAIHeartBeatRealtimeEntity implements Serializable {



	private  String CenterId;

	private  String SiteId;

	private  String AdaptorName;

	private  String AdaptorStage;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private Date Except_FirstSiteTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date Except_FirstHeadTime;

	private  String IsOccurLost;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date Lost_BeginTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date Lost_EndTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date ShortMessageTime;

	private  String AdaptorType;

	private  String AdaptorDesc;

	private  String StartMode;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date StartTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date EndTime;

	private  String ExceptInfo;

}
