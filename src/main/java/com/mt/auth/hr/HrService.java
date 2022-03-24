package com.mt.auth.hr;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mt.auth.rolemenu.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HrService {

  private final @NonNull HrOrgDeptMapper hrOrgDeptMapper;
  private final @NonNull HrUserEmpMapper hrUserEmpMapper;
  private final @NonNull RoleMenuService roleMenuService;
  private final @NonNull UserMenuMapper userMenuMapper;

  /**
   * 根据用户编码加载用户
   *
   * @param userId 用户编码
   * @return 用户对象
   */
  public HrUserEmp loadUser(String userId) {
    try {
      if (StringUtils.isEmpty(userId)) {
        return null;
      }
      return hrUserEmpMapper.findByUserId(userId);
    } catch (Exception ignored) {
      return null;
    }
  }

  /**
   * 根据用户登录名加载用户
   *
   * @param loginName 登录名
   * @return 用户对象
   */
  public HrUserEmp loadUserByLoginName(String loginName) {
    try {
      if (StringUtils.isEmpty(loginName)) {
        return null;
      }
      return hrUserEmpMapper.findByLoginName(loginName);
    } catch (Exception ignored) {
      return null;
    }
  }

  /**
   * 加载完整部门路径
   *
   * @param newDeptNo 部门编号
   */
  public String getFullDeptName(String newDeptNo) {
    ProcedureParam param = new ProcedureParam();
    param.setNewDeptNo(newDeptNo);
    hrUserEmpMapper.getFullDeptPath(param);
    return param.getFullName();
  }



  /**
   * 查看下属单位列表 (树状模式)
   *
   * @param newDeptNo 单位编号, 如果为空，表示所有单位
   * @return 单位列表 (子单位在 单位的 child 里)
   */
  public List<HrOrgDept> subDeptTree(String newDeptNo) {
    try {
      QueryWrapper<HrOrgDept> wrapper = new QueryWrapper<>();
      wrapper.eq("is_del", 0);
      if (!StringUtils.isEmpty(newDeptNo)) {
        wrapper.ne("dept_no", newDeptNo);
        wrapper.likeRight("dept_no", newDeptNo);
      }
      List<HrOrgDept> all = hrOrgDeptMapper.selectList(wrapper);
      //顶层单位
      List<HrOrgDept> topDepartments = new ArrayList<>();
      for (HrOrgDept temp : all) {
        if (temp.isTreeAdded()) {
          continue;
        }
        makeDeptTree(all, temp);
        topDepartments.add(temp);
      }
      return topDepartments;
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * 查找某单位用户列表，包括所欲子单位的用户
   *
   * @param newDeptNo 单位编号
   * @return 用户列表
   */
  public List<HrUserEmp> getDeptUsers(String newDeptNo) {
    try {
      if (StringUtils.isEmpty(newDeptNo)) {
        return new ArrayList<>();
      }
      return hrUserEmpMapper.selectUserDept(newDeptNo);
    } catch (Exception ignored) {
      return new ArrayList<>();
    }
  }

  /**
   * 分页加载所有用户
   *
   * @return 用户列表
   */
  public IPage selectUserPage(Page page) {
    try {
      return hrUserEmpMapper.selectUserPage(page);
    } catch (Exception ignored) {
      return null;
    }
  }

  /**
   * 查找用户
   *
   * @param cond 查询条件 支持3张表联查
   *             hr_user_emp u
   *             hr_org_comp c
   *             hr_org_dept d
   *             条件示例：  u.NEW_DEPT_NO like '124012%'
   * @return 用户列表
   */
  public List<HrUserEmp> searchUser(String cond) {
    try {
      if (StringUtils.isEmpty(cond)) {
        cond = "";
      }
      return hrUserEmpMapper.selectUserCond(cond);
    } catch (Exception ignored) {
      return new ArrayList<>();
    }
  }

  /**
   * 根据条件查询用户, 分页显示
   *
   * @return 用户列表
   */
  public IPage selectUserCondPage(String cond, Page page) {
    try {
      if (StringUtils.isEmpty(cond)) {
        cond = "";
      }
      return hrUserEmpMapper.selectUserCondPage(page, cond);
    } catch (Exception ignored) {
      return null;
    }
  }

  /**
   * 分页 查询某单位下的全部员工
   *
   * @return 用户列表
   */
  public IPage selectUserDeptPage(String newDeptNo, Page page) {
    try {
      if (StringUtils.isEmpty(newDeptNo)) {
        return null;
      }
      return hrUserEmpMapper.selectUserDeptPage(page, newDeptNo);
    } catch (Exception ignored) {
      return null;
    }
  }

  /**
   * 建立 单位 的树状结构
   */
  private static void makeDeptTree(List<HrOrgDept> all, HrOrgDept dept) {
    List<HrOrgDept> subs = new ArrayList<>();
    String newDeptNo = dept.getDeptNo();
    for (HrOrgDept temp : all) {
      if (temp.isTreeAdded()) {
        continue;
      }
      if (newDeptNo.equals(temp.getPDeptNo())) {
        subs.add(temp);
        temp.setTreeAdded(true);
      }
    }
    dept.setChild(subs);
    // 子项不为空时再添加子集
    if(subs.size()>0){

      dept.setChildren(subs);
    }

    dept.setValue(dept.getDeptNo());
    dept.setLabel(dept.getDeptName());

    for (HrOrgDept subDept : subs) {
      makeDeptTree(all, subDept);
    }
  }

  /**
   * 获取用户角色列表
   *
   * @return 角色列表
   */
  public List<Role> getUserRoles(HrUserEmp user) {
    List<Role> roles = user.getRoles();
    if (roles == null) {
      roles = roleMenuService.getUserRoles(user.getUserId());
      user.setRoles(roles);
    }
    return roles;
  }

  /**
   * 获取用户权限列表
   *
   * @return 权限列表
   */
  public List<String> getUserPerms(HrUserEmp user) {
    ArrayList<String> perms = new ArrayList<>();
    List<Role> roles = user.getRoles();
    if (roles == null) {
      roles = roleMenuService.getUserRoles(user.getUserId());
      user.setRoles(roles);
    }

    if (roles == null) {
      return perms;
    }

    for (Role role : roles) {
      List<String> rolePerms = roleMenuService.getRolePerms(role);
      for (String perm : rolePerms) {
        if (perms.contains(perm)) {
          continue;
        }
        perms.add(perm);
      }
    }

    return perms;
  }
  /**
   * 获取用户权限列表
   *
   * @return 权限列表
   */
  public List<String> getUserPermsNew(HrUserEmp user) {
    ArrayList<String> perms = new ArrayList<>();
   // String userId = user.getUserId();
   // List<Role> roles = user.getRoles();
   // if (roles == null) {
    List<Role> roles = roleMenuService.getUserRoles(user.getUserId());
    //  user.setRoles(roles);
   // }
    //通过userId查询当前用户的菜单
    List<Menu> userMenus = userMenuMapper.getUserMenus(user.getUserId());
    if (roles == null && userMenus == null) {
      return perms;
    }

    if(userMenus != null){
      List<String> userPerms = roleMenuService.getUserPerms(user.getUserId());
      for (String perm : userPerms) {
        if (perms.contains(perm)) {
          continue;
        }
        perms.add(perm);
      }
    }

    if(roles != null){
      for (Role role : roles) {
        List<String> rolePerms = roleMenuService.getRolePerms(role);
        for (String perm : rolePerms) {
          if (perms.contains(perm)) {
            continue;
          }
          perms.add(perm);
        }
      }
    }
    return perms;
  }
  /**
   * 用户否有某个权限
   *
   * @param user HrUserEmp 用户对象
   * @param perm 权限字符串
   * @return 是否
   */
  public boolean userHasPerm(HrUserEmp user, String perm) {
    if (StringUtils.isEmpty(perm)) {
      return false;
    }
    //List<String> perms = this.getUserPerms(user);
    //调整新方法
    List<String> perms = this.getUserPermsNew(user);
    return perms.contains(perm);
  }

  /**
   * 获取用户全部导航菜单(树状模式)
   *
   * @param user HrUserEmp用户对象
   * @return 树状菜单列表
   */
  public List<Menu> getUserMenuTree(HrUserEmp user) {
    List<Menu> all = new ArrayList<>();
    List<Role> roles = user.getRoles();
    //通过userId查询当前用户的菜单
    List<Menu> userMenus = userMenuMapper.getUserMenus(user.getUserId());
    if (roles == null) {
      //通过当前用户userId查询当前用户角色
      roles = roleMenuService.getUserRoles(user.getUserId());
      user.setRoles(roles);
    }
    if (roles == null && userMenus == null) {
      return all;
    }
    //人员菜单
    if(userMenus != null){
      for (Menu menu : userMenus) {
        if (menuListContains(all, menu)) {
          continue;
        }
        all.add(menu);
      }
    }

    //1. 合并所有role菜单
    for (Role role : roles) {
      //通过角色查询所有菜单
      List<Menu> roleMenus = roleMenuService.getRoleMenus(role);
      for (Menu menu : roleMenus) {
        if (menuListContains(all, menu)) {
          continue;
        }
        all.add(menu);
      }
    }

    all = MenuTree.filterMenu(all);
    Collections.sort(all);

    //2. 组织成树状结构
    List<Menu> topMenus = MenuTree.filterParent(all, 0);
    for (Menu menu : topMenus) {
      MenuTree.makeMenuTree(all, menu, false);
    }

    return topMenus;
  }
//  public List<Menu> getUserMenuTree(HrUserEmp user) {
//    List<Menu> all = new ArrayList<>();
//    List<Role> roles = user.getRoles();
//    if (roles == null) {
//      roles = roleMenuService.getUserRoles(user.getUserId());
//      user.setRoles(roles);
//    }
//    if (roles == null) {
//      return all;
//    }
//
//    //1. 合并所有role菜单
//    for (Role role : roles) {
//      List<Menu> roleMenus = roleMenuService.getRoleMenus(role);
//      for (Menu menu : roleMenus) {
//        if (menuListContains(all, menu)) {
//          continue;
//        }
//        all.add(menu);
//      }
//    }
//
//    all = MenuTree.filterMenu(all);
//    Collections.sort(all);
//
//    //2. 组织成树状结构
//    List<Menu> topMenus = MenuTree.filterParent(all, 0);
//    for (Menu menu : topMenus) {
//      MenuTree.makeMenuTree(all, menu, false);
//    }
//
//    return topMenus;
//  }

  private static boolean menuListContains(List<Menu> menus, Menu menu) {
    int menuId = menu.getMenuId();
    for (Menu m : menus) {
      if (m.getMenuId() == menuId) {
        return true;
      }
    }
    return false;
  }

  /**
   * 更新用户
   *
   * @param user 需要更新的用户
   * @return 是否成功
   */
  public boolean updateUser(HrUserEmp user) {
    try {
      return hrUserEmpMapper.updateById(user) == 1;
    } catch (Exception e) {
      return false;
    }
  }

}
