package com.mt.CenEAISiteRealtime.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 配电室信息
 * 
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@Data
@TableName("CenEAI_Site_Realtime")
public class CenEAISiteRealtimeEntity implements Serializable {


	private String CenterId;

	@TableField(exist = false)
	private  String CenterChineseName;

	private  String SiteId;

	private  String SiteChineseName;

	private  String SiteStatus;

	private  String SiteURL;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private Date SetupTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date StartupTime;

	private  String RunningStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date Status_FirstSiteTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date Status_FirstHeadTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date Heart_LastSiteTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
	private  Date Heart_LastHeadTime;

	private  Integer TimeDiffer;

	private  String OS_Info;

	private  String OS_User;

	private  String JDK_Version;

	private  String CENEAI_HOME;

	private  String NG_Version;

	private  String NG_BuildDate;

	private  String IP_Address;

	private  String HostName;

	private  Integer HeartFrequency;

	private  String ExceptInfo;

	@TableField(exist = false)
	private Integer num;




}
