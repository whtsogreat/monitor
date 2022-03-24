package com.mt.dictionary.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典实体类
 * @Author qingrui
 * @Date 20219/12/15 15:35
 * @Version 1.0
 */

@Data
@TableName("t_dictionary")
public class Dictionary {

    /**
     * 主键id
     */
    @TableId("id")
    private String id;

    /**
     * 字段名称
     */
    @TableField("field_key")
    private String fieldKey;

    /**
     * 字段常量
     */
    @TableField("constant_key")
    private String constantKey;

    /**
     * 字段编码（值）
     */
    @TableField("field_value")
    private String fieldValue;

    /**
     * 父级常量（id）
     */
    @TableField("parent_key")
    private String parentKey;

    /**
     * 字段排序
     */
    @TableField("field_sort")
    private Integer fieldSort;

    /**
     * 字段排序
     */
    @TableField("use_unit")
    private String useUnit;

    /**
     * 备注
     */
    @TableField("field_note")
    private String fieldNote;

    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("update_user")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否在页面显示（只有一级,YES,NO）
     */
    @TableField("is_page_show")
    private String isPageShow;

    /**
     * 数量
     */
    @TableField(exist = false)
    private String fieldCount;
    @TableField(exist = false)
    private String constantKeyA;
}
