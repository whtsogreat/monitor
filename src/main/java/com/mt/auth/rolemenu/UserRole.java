package com.mt.auth.rolemenu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 人员角色表管理
 * @Author qingrui
 * @Date 2021/3/2 16:15
 * @Version 1.0
 */
@Data
@TableName("t_user_role")
public class UserRole {

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("USER_ID")
    private String userId;


    /**
     * 角色id
     */
    @TableField(value = "ROLE_ID")
    private Integer roleId;
}
