package com.mt.CenEAISiteRealtime.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 前置机运行状态历史表
 * 
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@Data
@TableName("CenEAI_Site_RunningHistory")
public class CenEAISiteRunningHistoryEntity implements Serializable {


	/**
	 * 主键id
	 */
	@TableId(value = "Id",type = IdType.AUTO )
	private Integer Id;

	private  String CenterId;

	private  String SiteId;

	private  String RunningStatus;

	private  String ExceptInfo;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private Date StatusStartTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date StatusChangeSiteTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date StatusChangeHeadTime;


}
