package com.mt.auth.rolemenu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("t_menu")
public class Menu implements Serializable, Comparable<Menu> {

  @TableId(value = "MENU_ID", type = IdType.AUTO)
  private Integer menuId;

  /**
   * 父菜单ID
   */
  @TableField("PARENT_ID")
  private Integer parentId;

  /**
   * 父菜单名
   */
  @TableField(exist = false)
  private String parentName;

  /**
   * 菜单名
   */
  @TableField("MENU_NAME")
  private String menuName;

  /**
   * TAB名
   */
  @TableField("TAB_NAME")
  private String tabName;

  /**
   * 权限字符串
   */
  @TableField("PERM")
  private String perm;

  /**
   * 路由
   */
  @TableField("PATH")
  private String path;

  /**
   * 图标
   */
  @TableField("ICON")
  private String icon;

  /**
   * 类型 0菜单 1按钮
   */
  @TableField("MENU_TYPE")
  private Integer menuType;

  /**
   * 排序
   */
  @TableField("SERIAL")
  private Integer serial;

  /**
   * 是否系统菜单 0 否 1是
   */
  @TableField("IS_SYSTEM")
  private Integer isSystem;
  /**
   * 子菜单是否展开
   */
  @TableField("IS_OPEN")
  private Integer isOpen;

  /**
   * 是否有效 0 无效 1有效
   */
  @TableField("IS_ENABLED")
  private Integer isEnabled;
  /**
   * 子菜单(树状下一层)
   */
  @TableField(exist = false)
  private List<Menu> child;

  /**
   * 用于menu 排序
   */
  @Override
  public int compareTo(Menu menu) {
    return this.getSerial() - menu.getSerial();
  }

  public static final int SUB_MENU = 1;  //子菜单项,有效
  public static final int SUB_FUNC = 2;  //子功能项,有效
  public static final int SUB_MENU_FUNC = 3;  //全部子项，包括菜单和功能，有效
  public static final int SUB_MENU_ALL = 4;  //子菜单项, 不论是否有效
  public static final int SUB_FUNC_ALL = 5;  //子功能项, 不论是否有效
  public static final int SUB_MENU_FUNC_ALL = 6;  //全部子项，包括菜单和功能，无论是否有效

  public List<Menu> getSubMenus(int type) {
    if (child == null) return new ArrayList<>();
    List<Menu> menus;
    switch (type) {
      case SUB_MENU:
        menus = filterDisabled(child);
        return filterMenu(menus);
      case SUB_FUNC:
        menus = filterDisabled(child);
        return filterFunc(menus);
      case SUB_MENU_FUNC:
        return filterDisabled(child);
      case SUB_MENU_ALL:
        return filterMenu(child);
      case SUB_FUNC_ALL:
        return filterFunc(child);
      case SUB_MENU_FUNC_ALL:
        return child;
      default:
        return new ArrayList<>();
    }
  }

  //过滤无效的菜单
  private static List<Menu> filterDisabled(List<Menu> all) {
    ArrayList<Menu> menus = new ArrayList<>();
    for (Menu menu : all) {
      if (menu.getIsEnabled() == 1) {
        menus.add(menu);
      }
    }
    return menus;
  }

  //仅保留菜单项
  private static List<Menu> filterMenu(List<Menu> all) {
    ArrayList<Menu> menus = new ArrayList<>();
    for (Menu menu : all) {
      if (menu.getMenuType() != 1) {  //0, 2为菜单
        menus.add(menu);
      }
    }
    return menus;
  }

  //仅保留功能项
  private static List<Menu> filterFunc(List<Menu> all) {
    ArrayList<Menu> menus = new ArrayList<>();
    for (Menu menu : all) {
      if (menu.getMenuType() == 1) {
        menus.add(menu);
      }
    }
    return menus;
  }


}
