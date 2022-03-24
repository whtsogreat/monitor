package com.mt.auth.rolemenu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_role")
public class Role implements Serializable {

  @TableId(value = "ROLE_ID", type = IdType.AUTO)
  private Integer roleId;

  /**
   * 角色名
   */
  @TableField("ROLE_NAME")
  private String roleName;

  /**
   * 描述
   */
  @TableField("REMARK")
  private String remark;

  /**
   * 是否系统角色 0 否 1是
   */
  @TableField("IS_SYSTEM")
  private Integer isSystem;

  /**
   * 包含的菜单项 (全部，包括失效的)
   * 程序手工加载
   */
  @TableField(exist = false)
  private List<Menu> menus;
  /**
   * 创建者
   */
//  @TableField("CREATE_USER")
//  private Long createUser;

  /**
   * 创建时间
   */
//  @TableField(value = "CREATE_TIME")
//  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//  private Date createTime;

  /**
   * 是否有效 0 无效 1有效
   */
//  @TableField("IS_ENABLED")
//  private Integer isEnabled;

  /**
   * 权限ID列表  1,2,3,4
   * 仅在后台编辑Role时使用
   */
  @TableField(exist = false)
  private String menuIdList;
  /**
   * 包含的全部菜单功能, 不论是否enabled
   * 平面模式, 服务器端使用，输出时删除
   */
  @JsonIgnore
  @TableField(exist = false)
  private List<Menu> roleMenusAll;


  public static final int MENU_PANE_ALL = 1;  //平面模式，全部(不论是否有效，菜单，功能)
  public static final int MENU_PANE_ENABLED = 2;  //平面模式，有效(菜单，功能)
  public static final int MENU_PANE_MENU_ENABLED = 3;  //平面模式，有效的菜单
  public static final int MENU_PANE_FUNC_ENABLED = 4;  //平面模式，有效的功能
  public static final int MENU_TOP_ALL = 5;  //树状模式，全部(不论是否有效，菜单，功能)
  public static final int MENU_TOP_ENABLED = 6;  //树状模式，有效(菜单，功能)
  public static final int MENU_TOP_MENU_ENABLED = 7;  //树状模式，有效的菜单

  public List<Menu> getRoleMenus(int type) {
    if (roleMenusAll == null) return new ArrayList<>();
    List<Menu> menus;
    switch (type) {
      case MENU_PANE_ALL:
        return roleMenusAll;
      case MENU_PANE_ENABLED:
        return filterDisabled(roleMenusAll);
      case MENU_PANE_MENU_ENABLED:
        menus = filterDisabled(roleMenusAll);
        return filterMenu(menus);
      case MENU_PANE_FUNC_ENABLED:
        menus = filterDisabled(roleMenusAll);
        return filterFunc(menus);
      case MENU_TOP_ALL:
        menus = filterParent(roleMenusAll, 0);
        for (Menu menu: menus) {
          makeMenuTree(roleMenusAll, menu, false, false, false);
        }
        return menus;
      case MENU_TOP_ENABLED:
        menus = filterDisabled(roleMenusAll);
        menus = filterParent(roleMenusAll, 0);
        for (Menu menu: menus) {
          makeMenuTree(roleMenusAll, menu, true, false, false);
        }
        return menus;
      case MENU_TOP_MENU_ENABLED:
        menus = filterDisabled(roleMenusAll);
        menus = filterMenu(menus);
        menus = filterParent(roleMenusAll, 0);
        for (Menu menu: menus) {
          makeMenuTree(roleMenusAll, menu, true, true, false);
        }
        return menus;
      default:
        return new ArrayList<>();
    }
  }

  /**
   * 获取全部权限列表
   * @return 权限列表
   */
  public List<String> getPerms() {
    ArrayList<String> perms = new ArrayList<>();
    if (roleMenusAll == null) return perms;

    List<Menu> menus = getRoleMenus(MENU_PANE_ENABLED);
    if (menus == null) return perms;
    for (Menu menu : menus) {
      String perm = menu.getPerm();
      if (StringUtils.isEmpty(perm)) continue;
      if (perms.contains(perm)) continue;
      perms.add(perm);
    }
    return perms;
  }

  /**
   * 计算menuIdList
   */
  public void calcMenuIdList() {
    this.setMenuIdList("");
    if (roleMenusAll == null) return;
    List<Menu> menus = getRoleMenus(MENU_PANE_ENABLED);
    if (menus == null) return;
    List<Integer> ids = new ArrayList<>();
    for (Menu menu : menus) {
      boolean exist = false;
      for (int id : ids) {
        if (id == menu.getMenuId()) {
          exist = true;
          break;
        }
      }
      if (exist) continue;
      ids.add(menu.getMenuId());
    }
    StringBuilder builder = new StringBuilder();
    boolean first = true;
    for (int id : ids) {
      if (first) {
        first = false;
      } else {
        builder.append(",");
      }
      builder.append(id);
    }
    this.setMenuIdList(builder.toString());
  }

  /**
   * 是否有某个权限
   * @param perm 权限字符串
   * @return 是否
   */
  public boolean hasPerm(String perm) {
    if (StringUtils.isEmpty(perm)) return false;
    if (roleMenusAll == null) return false;

    List<Menu> menus = getRoleMenus(MENU_PANE_ENABLED);
    if (menus == null) return false;
    for (Menu menu : menus) {
      if (perm.equals(menu.getPerm())) {
        return true;
      }
    }
    return false;
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

  //查找parent为特定id的列表
  private static List<Menu> filterParent(List<Menu> all, int parentId) {
    ArrayList<Menu> menus = new ArrayList<>();
    for (Menu menu : all) {
      if (menu.getParentId() == parentId) {
        menus.add(menu);
      }
    }
    return menus;
  }

  // 建立 menu 的树状结构
  private static void makeMenuTree(List<Menu> all, Menu menu, boolean onlyEnabled, boolean onlyMenu, boolean onlyFunc) {
    //1. 查找menu的所有sub menus
    List<Menu> subMenus = filterParent(all, menu.getMenuId());
    //2. 过滤不需要的sub menus
    if (onlyEnabled) subMenus = filterDisabled(subMenus);
    if (onlyMenu) subMenus = filterMenu(subMenus);
    if (onlyFunc) subMenus = filterFunc(subMenus);
    menu.setChild(subMenus);
    //3. 递归查找 sub menus 的 sub menus
    for (Menu subMenu: subMenus) {
      makeMenuTree(all, subMenu, onlyEnabled, onlyMenu, onlyFunc);
    }
  }
}
