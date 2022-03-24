package com.mt.auth.rolemenu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 人员菜单mapper
 * @Author qingrui
 * @Date 2021/3/8 10:27
 * @Version 1.0
 */
@Component
public interface UserMenuMapper extends BaseMapper<UserMenu> {

    /**
     * 查询userId下的所有menus
     * 平面模式
     *
     * @param userId 角色id
     * @return 菜单列表
     */
    @Select("SELECT m.* FROM t_menu m \n" +
            "WHERE m.MENU_ID IN ( SELECT DISTINCT r.MENU_ID FROM t_user_menu r WHERE r.USER_ID=#{userId})")
    List<Menu> getUserMenus(@Param("userId") String userId);



    /**
     * 删除用户的一个菜单
     *
     * @param userId 用户编号
     * @param menuId 菜单ID
     * @return 影响的行数
     */
    @Delete("DELETE from t_user_menu where USER_ID=#{userId} and MENU_ID=#{menuId}")
    int removeUserMenu(@Param("userId") String userId, @Param("menuId") int menuId);

}
