package com.mt.auth.rolemenu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MenuMapper extends BaseMapper<Menu> {

  /**
   * 加载menu或功能
   *
   * @param id 菜单/功能ID
   * @return Menu
   */
  @Select("SELECT * from t_menu where MENU_ID=#{id}")
  Menu getMenu(@Param("id") int id);

  /**
   * 加载子菜单
   *
   * @param parentId 父菜单ID
   * @return Menu列表
   */
  @Select("SELECT * from t_menu where PARENT_ID=#{parentId}")
  List<Menu> getSubMenus(@Param("parentId") int parentId);

  /**
   * 获取所有菜单 (用于后台管理)
   * 非树状结构，平面形式列表出所有菜单
   *
   * @return Menu列表
   */
  @Select("SELECT * from t_menu order by serial asc")
  List<Menu> getAllMenus();

}
