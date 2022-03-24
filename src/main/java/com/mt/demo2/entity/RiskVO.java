package com.mt.demo2.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: wss
 * @Date: 15:11 2021/7/1
 * @Description: excel内容
 */
@Data
@TableName("major_case_track")
public class RiskVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键UUID
     */
    @TableId
    private String id;
    /**
     * 涉案老年人姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 年龄
     */
    private String age;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 身份证号
     */
    private String idnumber;
    /**
     * 案由
     */
    private String reason;
    /**
     * 案情介绍
     */
    private String introduction;
    /**
     * 进展情况
     */
    private String process;
    /**
     * 数据创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createtime;
}
