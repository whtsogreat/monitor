package com.mt.auth.rolemenu;

import java.util.ArrayList;
import java.util.List;

public class MenuTree {

  /**
   * 建立 menu 的树状结构
   */
  public static void makeMenuTree(List<Menu> all, Menu menu, boolean onlyMenu) {
    List<Menu> subMenus = filterParent(all, menu.getMenuId());
    if (onlyMenu) {
      subMenus = filterMenu(subMenus);
    }
    menu.setChild(subMenus);
    //递归查找 sub menus 的 sub menus
    for (Menu subMenu : subMenus) {
      makeMenuTree(all, subMenu, onlyMenu);
    }
  }

  /**
   * 查找parent为特定id的列表
   */
  public static List<Menu> filterParent(List<Menu> all, int parentId) {
    ArrayList<Menu> menus = new ArrayList<>();
    for (Menu menu : all) {
      if (menu.getParentId() == parentId) {
        menus.add(menu);
      }
    }
    return menus;
  }

  /**
   * 仅保留菜单项
   */
  public static List<Menu> filterMenu(List<Menu> all) {
    ArrayList<Menu> menus = new ArrayList<>();
    for (Menu menu : all) {
      if (menu.getMenuType() != 1) {
        // 1 是按钮
        menus.add(menu);
      }
    }
    return menus;
  }

}
