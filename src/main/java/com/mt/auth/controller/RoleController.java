package com.mt.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mt.auth.hr.HrService;
import com.mt.auth.hr.HrUserEmp;
import com.mt.auth.rolemenu.*;
import com.mt.auth.utils.JsonResponse;
import com.mt.auth.utils.LoginUserService;
import com.mt.auth.utils.TokenUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Role管理
 * 后台管理用
 */

@RestController
@RequiredArgsConstructor
public class RoleController {

  private final @NonNull LoginUserService loginUserService;
  private final @NonNull HrService hrService;
  private final @NonNull RoleMenuService roleMenuService;
  private final @NonNull RoleMapper roleMapper;
  private final @NonNull TokenUtils tokenUtils;

  /**
   * 角色列表
   * 权限: sys:role:list
   */
  @RequestMapping("/sys/roles")
  public JsonResponse getRoleListAll() {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:role:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      response.put("roles", roleMenuService.getAllRoles());
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }

  /**
   * 分页查询角色列表
   * 权限: sys:role:list
   *
   * @param roleName 查询角色名，空表示全部
   * @param pageNo   第几页
   * @param pageSize 每页数量
   * @return 角色列表
   */
  @RequestMapping("/sys/role/list")
  public JsonResponse getRolePage(String roleName, Integer pageNo, Integer pageSize) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      HrUserEmp user = tokenUtils.getUserInfo();
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:role:list")) {
        return response.setFailed(1, "没有访问权限");
      }
      if (user == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(user, "sys:role:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      //检查参数
      if (pageNo == null) {
        pageNo = 1;
      }
      if (pageSize == null) {
        pageSize = 10;
      }
      Page<Role> page = new Page<>(pageNo, pageSize);
      QueryWrapper<Role> wrapper = new QueryWrapper<>();
      if (!StringUtils.isEmpty(roleName)) {
        wrapper.like("ROLE_NAME", roleName);
      }
      response.put("page", roleMapper.selectPage(page, wrapper));
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }

  /**
   * 新增角色
   * 权限: sys:role:add
   *
   * @param role 角色
   */
  @RequestMapping("/sys/role/add")
  @Transactional(rollbackFor = Exception.class)
  public JsonResponse roleAdd(Role role, String menuIdList) {
    JsonResponse response = new JsonResponse();
    try {
      if (role == null) {
        return response.setFailed(1, "参数不正确");
      }
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:role:add")) {
        return response.setFailed(2, "没有访问权限");
      }

      if (!roleMenuService.addRule(role)) {
        return response.setFailed(3, "添加角色失败，请保证角色名称不重复");
      }

      //TODO：批量插入, 校验菜单是否存在？

      if (!StringUtils.isEmpty(menuIdList)) {
        for (String menuId : menuIdList.split(",")) {
          int id = Integer.parseInt(menuId);
          if (!roleMenuService.roleAddMenu(role.getRoleId(), id)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response.setFailed(4, "数据保存失败");
          }
        }
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        return response.setFailed(5, "保存失败，请保证角色名称不重复");
      }
      return response.setFailed(6, e.getLocalizedMessage());
    }
  }

  /**
   * 修改角色
   * 权限: sys:role:edit
   *
   * @param role 角色
   */
  @RequestMapping("/sys/role/edit")
  @Transactional(rollbackFor = Exception.class)
  public JsonResponse roleEdit(Role role, String menuIdList) {
    JsonResponse response = new JsonResponse();
    try {
      if (role == null) {
        return response.setFailed(1, "参数不正确");
      }
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:role:edit")) {
        return response.setFailed(2, "没有访问权限");
      }

      if (!roleMenuService.updateRole(role)) {
        return response.setFailed(3, "添加角色失败，请保证角色名称不重复");
      }

      roleMenuService.roleClearMenus(role.getRoleId());

      //TODO：批量插入, 校验菜单是否存在？

      if (!StringUtils.isEmpty(menuIdList)) {
        for (String menuId : menuIdList.split(",")) {
          int id = Integer.parseInt(menuId);
          if (!roleMenuService.roleAddMenu(role.getRoleId(), id)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response.setFailed(4, "数据保存失败");
          }
        }
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      if (e instanceof DuplicateKeyException) {
        return response.setFailed(5, "保存失败，请保证角色名称不重复");
      }
      return response.setFailed(6, e.getLocalizedMessage());
    }
  }

  /**
   * 加载Role
   * 不加载role menus
   * 权限: sys:role:list
   *
   * @param id 角色ID
   */
  @GetMapping("/sys/role/info/{id}")
  public JsonResponse getRole(@PathVariable("id") Integer id) {
    JsonResponse response = new JsonResponse();
    try {
      if (id == null) {
        return response.setFailed(1, "参数不正确");
      }
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:role:list")) {
        return response.setFailed(2, "没有访问权限");
      }

      Role role = roleMenuService.getRole(id);
      if (role == null) {
        return response.setFailed(3, "记录不存在");
      }

      List<Menu> menus = roleMenuService.getRoleMenus(role);
      response.put("menuIdList", calcMenuIdList(menus));
      response.put("role", role);

      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(4, e.getLocalizedMessage());
    }
  }

  /**
   * 删除角色
   * 权限: sys:role:delete
   *
   * @param id 角色ID
   */
  @RequestMapping("/sys/role/delete")
  public JsonResponse roleDelete(Integer id) {
    JsonResponse response = new JsonResponse();
    try {
      if (id == null) {
        return response.setFailed(1, "参数不正确");
      }
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:role:delete")) {
        return response.setFailed(2, "没有访问权限");
      }

      //删除 角色权限
      roleMenuService.roleClearMenus(id);
      //删除角色的全部用户
      roleMenuService.roleClearUsers(id);

      if (!roleMenuService.deleteRole(id)) {
        return response.setFailed(3, "数据库错误导致删除失败");
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(4, e.getLocalizedMessage());
    }
  }

  /**
   * 修改角色权限
   * 权限: sys:role:edit
   *
   * @param roleId 角色ID
   * @param menus  菜单ID列表 (逗号分隔IDs)
   */
  @RequestMapping("/role/setRoleMenus")
  @Transactional(rollbackFor = Exception.class)
  public JsonResponse roleSetMenus(Integer roleId, String menus) {
    JsonResponse response = new JsonResponse();
    try {
      if (roleId == null || menus == null) {
        return response.setFailed(1, "参数不正确");
      }
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:role:edit")) {
        return response.setFailed(2, "没有访问权限");
      }

      Role role = roleMenuService.getRole(roleId);
      if (role == null) {
        return response.setFailed(3, "角色不存在");
      }

      roleMapper.clearRoleMenus(roleId);

      //TODO：批量插入, 校验菜单是否存在？

      for (String menuId : menus.split(",")) {
        int id = Integer.parseInt(menuId);
        if (!roleMenuService.roleAddMenu(roleId, id)) {
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          return response.setFailed(4, "数据库操作错误");
        }
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return response.setFailed(5, e.getLocalizedMessage());
    }
  }

  /**
   * 计算menuIdList
   */
  private String calcMenuIdList(List<Menu> menus) {
    String menuIdList = "";
    if (menus == null || menus.isEmpty()) {
      return menuIdList;
    }
    List<Integer> ids = new ArrayList<>();
    for (Menu menu : menus) {
      int id = menu.getMenuId();
      if (ids.contains(id)) {
        continue;
      }
      ids.add(id);
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
    return builder.toString();
  }


  /**
   * 查看用户详情
   * 查询角色
   * 权限：sys:user:list
   */
  @RequestMapping("/sys/user/roledetailNew/{userId}")
  public JsonResponse userDetailNew(@PathVariable String userId) {
    JsonResponse response = new JsonResponse();
    try {
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
//      //权限校验
//      if (!hrService.userHasPerm(loginUser, "sys:user:list")) {
//        return response.setFailed(1, "没有访问权限");
//      }
      //检查参数
      if (StringUtils.isEmpty(userId)) {
        return response.setFailed(2, "参数不正确");
      }
     // HrUserEmp detailUser = hrService.loadUser(userId);
//      if (detailUser != null && detailUser.getNewDeptNo() != null) {
//        detailUser.setFullDeptName(detailUser.getCompName() + hrService.getFullDeptName(detailUser.getNewDeptNo()));
//        //处理特殊的岗位
//        if ("00000000".equals(detailUser.getPostNo())) {
//          detailUser.setPostName("");
//        }
//      }
//      response.put("user", detailUser);
      if (userId != null) {
        response.put("roles", roleMapper.getUserRoles(userId));
      } else {
        response.put("roles", "{}");
      }
      response.put("allRoles", roleMenuService.getAllRoles());
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(3, e.getLocalizedMessage());
    }
  }

  /**
   * 根据用户id加载Role
   * 不加载role menus
   * 权限: sys:role:list
   *
   * @param userId 用户ID
   */
  @GetMapping("/sys/userRole/info/{userId}")
  public JsonResponse getRoleMenu(@PathVariable("userId") String userId) {
    JsonResponse response = new JsonResponse();
    try {
      if (userId == null) {
        return response.setFailed(1, "参数不正确");
      }
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }

      HrUserEmp user = tokenUtils.getUserInfo();
      if(user == null){
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(user, "sys:user:edit")) {
        return response.setFailed(2, "没有访问权限");
      }

      //获取当前用户的全部角色,去重
      List<Role> userRoles = roleMapper.getUserRoles(userId);
      if(userRoles == null) {
        return response.setFailed(3, "记录不存在");
      }
      List<Menu> menus = new ArrayList<Menu>();
      //获取每个角色下的所有菜单
      for (int i = 0; i <userRoles.size() ; i++) {

        List<Menu> menus2 = roleMenuService.getRoleMenus(userRoles.get(i));
        //获取的菜单集合合并在一起,并去重
        menus.addAll(menus2);
        menus = new ArrayList<Menu>(new LinkedHashSet<>(menus));

      }

      //查询角色下所有的菜单
//      Role role = roleMenuService.getRole(userId);
//      if (role == null) {
//        return response.setFailed(3, "记录不存在");
//      }
//
//      List<Menu> menus = roleMenuService.getRoleMenus(role);
      response.put("menuIdList", calcMenuIdList(menus));
//      response.put("role", role);

      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(4, e.getLocalizedMessage());
    }
  }


}
