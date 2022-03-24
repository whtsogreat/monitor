package com.mt.CenEAISiteRealtime.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 交换类型名称
 * 
 * @author censoft
 * @date 2021-11-15 14:45:41
 */
@Data
@TableName("dic_center")
public class DicCenterEntity implements Serializable {


	private String code;

	private  String remark;


}
