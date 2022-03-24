package com.mt.auth.rolemenu;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleMenuService {

  private final @NonNull RoleMapper roleMapper;
  private final @NonNull MenuMapper menuMapper;
  private final @NonNull UserMenuMapper userMenuMapper;

  /**
   * 系统全部角色列表
   *
   * @return 角色列表
   */
  public List<Role> getAllRoles() {
    try {
      return roleMapper.getAllRoles();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * 加载Role
   */
  public Role getRole(int roleId) {
    try {
      return roleMapper.getRole(roleId);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 新增Role
   */
  public boolean addRule(Role role) {
    try {
      return roleMapper.insertRole(role) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 修改保存Role (仅修改role表)
   */
  public boolean updateRole(Role role) {
    try {
      return roleMapper.updateById(role) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 删除Role
   */
  public boolean deleteRole(int roleId) {
    try {
      return roleMapper.deleteById(roleId) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 给Role 增加一个权限
   */
  public boolean roleAddMenu(int roleId, int menuId) {
    try {
      return roleMapper.addRoleMenu(roleId, menuId) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 清空role的全部权限
   */
  public void roleClearMenus(int roleId) {
    try {
      roleMapper.clearRoleMenus(roleId);
    } catch (Exception ignored) {
    }
  }

  /**
   * 加载权限
   */
  public Menu getMenu(int menuId) {
    try {
      return menuMapper.getMenu(menuId);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 加载子权限
   */
  public List<Menu> getSubMenu(int parentMenuId) {
    try {
      return menuMapper.getSubMenus(parentMenuId);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 添加权限
   */
  public boolean addMenu(Menu menu) {
    try {
      return menuMapper.insert(menu) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 修改权限
   */
  public boolean updateMenu(Menu menu) {
    try {
      return menuMapper.updateById(menu) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 删除Menu
   */
  public boolean deleteMenu(int menuId) {
    try {
      return menuMapper.deleteById(menuId) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 加载全局所有 权限列表 （树状结构）
   */
  public List<Menu> getMenuTree() {
    try {
      List<Menu> all = menuMapper.getAllMenus();
      if (all == null) {
        all = new ArrayList<>();
      }

      List<Menu> topMenus = MenuTree.filterParent(all, 0);
      for (Menu menu : topMenus) {
        MenuTree.makeMenuTree(all, menu, false);
      }
      return topMenus;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * 用户添加一个角色
   */
  public boolean userAddRole(String userId, int roleId) {
    try {
      return roleMapper.addUserRole(userId, roleId) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 用户减少一个角色
   */
  public boolean userRemoveRole(String userId, int roleId) {
    try {
      return roleMapper.removeUserRole(userId, roleId) == 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 清除用户的全部角色
   */
  public void userClearRoles(String userId) {
    try {
      roleMapper.removeUserAllRole(userId);
    } catch (Exception ignored) {
    }
  }

  /**
   * 清除角色的全部用户 (删除角色时用)
   */
  public void roleClearUsers(int roleId) {
    try {
      roleMapper.removeRoleAllUser(roleId);
    } catch (Exception ignored) {
    }
  }

  /**
   * 加载用户的全部角色
   */
  public List<Role> getUserRoles(String userId) {
    try {
      return roleMapper.getUserRoles(userId);
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * 获取Role的全部权限列表
   *
   * @return 权限列表
   */
  public List<Menu> getRoleMenus(Role role) {
    try {
      List<Menu> menus = role.getMenus();
      if (menus == null) {
        menus = roleMapper.getRoleMenus(role.getRoleId());
        role.setMenus(menus);
      }
      return menus;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * 获取Role的全部权限列表
   *
   * @return 权限列表
   */
  public List<String> getRolePerms(Role role) {
    ArrayList<String> perms = new ArrayList<>();
    List<Menu> menus = getRoleMenus(role);
    if (menus == null) {
      return perms;
    }
    for (Menu menu : menus) {
      String perm = menu.getPerm();
      if (StringUtils.isEmpty(perm)) {
        continue;
      }
      if (perms.contains(perm)) {
        continue;
      }
      perms.add(perm);
    }
    return perms;
  }


  /**
   * 获取人员菜单的全部权限列表
   *
   * @return 权限列表
   */
  public List<String> getUserPerms(String userId) {
    ArrayList<String> perms = new ArrayList<>();
    List<Menu> menus = userMenuMapper.getUserMenus(userId);
   // List<Menu> menus = getRoleMenus(role);
    if (menus == null) {
      return perms;
    }
    for (Menu menu : menus) {
      String perm = menu.getPerm();
      if (StringUtils.isEmpty(perm)) {
        continue;
      }
      if (perms.contains(perm)) {
        continue;
      }
      perms.add(perm);
    }
    return perms;
  }
}
