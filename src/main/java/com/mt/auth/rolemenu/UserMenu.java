package com.mt.auth.rolemenu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *
 * 人员菜单表管理
 * @Author qingrui
 * @Date 2021/3/8 10:23
 * @Version 1.0
 */
@Data
@TableName("t_user_menu")
public class UserMenu {


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
     * 菜单id
     */
    @TableField("MENU_ID")
    private Integer menuId;
}
