package com.mt.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mt.auth.hr.HrService;
import com.mt.auth.hr.HrUserEmp;
import com.mt.auth.hr.HrUserEmpMapper;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HrController {

  private final @NonNull LoginUserService loginUserService;
  private final @NonNull HrService hrService;
  private final @NonNull RoleMenuService roleMenuService;
  private final @NonNull HrUserEmpMapper hrUserEmpMapper;
  private final @NonNull TokenUtils tokenUtils;
  private final @NonNull RoleMapper roleMapper;

  /**
   * 加载任意用户信息
   *
   * @param userId 用户编号
   * @return HrUserEmp 对象
   */
  @RequestMapping("/user/info/{userId}")
  public JsonResponse userInfo(@PathVariable String userId) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //TODO: 修改成需要的权限字符串
      if (!hrService.userHasPerm(loginUser, "sys:user:list")) {
        return response.setFailed(1, "没有权限");
      }
      HrUserEmp user = hrService.loadUser(userId);
      //处理特殊的岗位
      if (user != null && "00000000".equals(user.getPostNo())) {
        user.setPostName("");
      }
      response.put("user", user);
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getMessage());
    }
  }

  /**
   * 当前登录用户信息
   * 权限：-
   *
   * @return WebResponse
   */
  @RequestMapping("/sys/user/info")
  public JsonResponse userInfo() {
    JsonResponse response = new JsonResponse();
    try {
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
      HrUserEmp user = tokenUtils.getUserInfo();
      if (user == null) {
        return response;
      }
      //处理特殊的岗位
      if ("00000000".equals(user.getPostNo())) {
        user.setPostName("");
      }

      String userId = user.getUserId();



      response.put("user", user);
     // response.put("perms", hrService.getUserPerms(loginUser));
      response.put("perms", hrService.getUserPermsNew(user));
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(1, e.getMessage());
    }
  }


  /**
   * 当前登录用户信息
   * 权限：-
   *
   * @return WebResponse
   */
  @RequestMapping("/sys/user/info_all")
  public JsonResponse userInfoAll() {
    JsonResponse response = new JsonResponse();
    try {
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
      HrUserEmp user = tokenUtils.getUserInfo();
      if (user == null) {
        return response;
      }


      response.put("user", user);
      // response.put("perms", hrService.getUserPerms(loginUser));
      response.put("perms", hrService.getUserPermsNew(user));
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(1, e.getMessage());
    }
  }


  /**
   * 查看用户导航菜单和权限(树状菜单)
   * 权限：无
   *
   * @return WebResponse
   */
  @RequestMapping("/sys/user/menuTree")
  public JsonResponse sysUserMenuTree() {
    JsonResponse response = new JsonResponse();
    try {
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
      HrUserEmp user = tokenUtils.getUserInfo();
      if (user == null) {
        return response;
      }

      //获取树状菜单列表
     // List<Menu> menuList = hrService.getUserMenuTree(loginUser);
      List<Menu> menuList = hrService.getUserMenuTree(user);
      menuList = MenuTree.filterMenu(menuList);

      //格式化菜单列表
      formatMenuVue(menuList, null);
      response.put("menuList", menuList);
     // response.put("perms", hrService.getUserPerms(loginUser));
      response.put("perms", hrService.getUserPermsNew(user));
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(1, e.getMessage());
    }
  }

  /**
   * 为前端vue准备menu
   * 设置菜单上级名称
   */
  private void formatMenuVue(List<Menu> menuList, Menu parent) {
    for (Menu menu : menuList) {
      if (parent != null) {
        menu.setParentName(parent.getMenuName());
      }
      formatMenuVue(menu.getChild(), menu);
    }
  }

  /**
   * 分页查询本地用户列表
   * 权限: sys:user:list
   *
   * @param searchStr 模糊查询字符串 (用户编号，姓名)
   * @param newDeptNo    用户所在部门
   * @param pageNo    第几页
   * @param pageSize  每页数量
   * @return 用户列表
   */
  @RequestMapping("/sys/user/list")
  public JsonResponse searchUser(String searchStr, String newDeptNo, Integer pageNo, Integer pageSize) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:user:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      if (pageNo == null) {
        pageNo = 1;
      }
      if (pageSize == null) {
        pageSize = 10;
      }
      Page<HrUserEmp> page = new Page<>(pageNo, pageSize);
      String cond = "";
      if (!StringUtils.isEmpty(searchStr)) {
        cond += " AND (" +
            " u.USER_ID like '" + "%" + searchStr + "%' OR " +
            " u.NEW_EMP_NO like '" + "%" + searchStr + "%' OR " +
            " u.NEW_DEPT_NO like '" + "%" + searchStr + "%' OR " +
            " u.EMP_NAME like '" + "%" + searchStr + "%' " +
            ") ";
      }
      if (!StringUtils.isEmpty(newDeptNo)) {
        cond += " AND u.NEW_DEPT_NO like '" + newDeptNo + "%' ";
      }

      cond += " order by u.NEW_EMP_NO asc ";
      response.put("page", hrService.selectUserCondPage(cond, page));
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }

  /**
   * 查看用户详情
   * 权限：sys:user:list
   */
  @RequestMapping("/sys/user/detail/{userId}")
  public JsonResponse userDetail(@PathVariable String userId) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:user:list")) {
        return response.setFailed(1, "没有访问权限");
      }
      //检查参数
      if (StringUtils.isEmpty(userId)) {
        return response.setFailed(2, "参数不正确");
      }

      HrUserEmp detailUser = hrService.loadUser(userId);
      if (detailUser != null && detailUser.getNewDeptNo() != null) {
        detailUser.setFullDeptName(detailUser.getCompName() + hrService.getFullDeptName(detailUser.getNewDeptNo()));
        //处理特殊的岗位
        if ("00000000".equals(detailUser.getPostNo())) {
          detailUser.setPostName("");
        }
      }
      response.put("user", detailUser);
      if (detailUser != null) {
        response.put("roles", hrService.getUserRoles(detailUser));
      } else {
        response.put("roles", "{}");
      }
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(3, e.getLocalizedMessage());
    }
  }

  /**
   * 请求用户角色
   * 前端： 用户列表/设置角色 页面
   * 权限：sys:user:list
   */
  @RequestMapping("/sys/user/role/{userId}")
  public JsonResponse userRoles(@PathVariable String userId) {
    JsonResponse response = new JsonResponse();
    try {
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
      HrUserEmp user = tokenUtils.getUserInfo();
      if (user == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(user, "sys:user:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      response.put("user2", user);
      //检查参数
      if (StringUtils.isEmpty(userId)) {
        return response.setFailed(2, "参数不正确");
      }

     // HrUserEmp detailUser = hrService.loadUser(userId);
   //   response.put("user", detailUser);
   //   response.put("userRoles", hrService.getUserRoles(detailUser));
    //  response.put("allRoles", roleMenuService.getAllRoles());
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(3, e.getLocalizedMessage());
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
   * 设置用户角色 (具体操作)
   * 权限: sys:user:edit
   *
   * @param userId 用户ID
   * @param roles  角色ID...
   */
  @RequestMapping("/sys/user/setroles")
  @Transactional(rollbackFor = Exception.class)
  public JsonResponse userSetRoles(String userId, String roles) {
    JsonResponse response = new JsonResponse();
    try {
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
//      //权限校验
//      if (!hrService.userHasPerm(loginUser, "sys:user:edit")) {
//        return response.setFailed(1, "没有访问权限");
//      }
      HrUserEmp user = tokenUtils.getUserInfo();
      if (user == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(user, "sys:user:edit")) {
        return response.setFailed(1, "没有访问权限");
      }
      if (userId == null || roles == null) {
        return response.setFailed(2, "参数错误");
      }

   //   HrUserEmp detailUser = hrService.loadUser(userId);
    //  List<Role> userRoles = hrService.getUserRoles(detailUser);
      List<Role> userRoles = roleMapper.getUserRoles(userId);
      if (userRoles == null) {
        userRoles = new ArrayList<>();
      }

      String[] roleIds = roles.split(",");
      int id;
      for (String sId : roleIds) {
        try {
          id = Integer.parseInt(sId);
        } catch (Exception e) {
          continue;
        }
        boolean found = false;
        for (Role role : userRoles) {
          if (role.getRoleId() == id) {
            found = true;
            //标记已经处理了
            role.setRoleId(0);
            break;
          }
        }
        if (found) {
          continue;
        }
        //新的授权
        if (!roleMenuService.userAddRole(userId, id)) {
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          return response.setFailed(3, "发生异常错误");
        }
      }

      //查找已经被删除的授权
      for (Role role : userRoles) {
        if (role.getRoleId() != 0) {
          if (!roleMenuService.userRemoveRole(userId, role.getRoleId())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response.setFailed(4, "发生异常错误");
          }
        }
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return response.setFailed(5, e.getLocalizedMessage());
    }
  }

  /**
   * 删除用户
   * 权限: sys:user:delete
   */
  @RequestMapping("/sys/user/delete")
  public JsonResponse deleteUser(String userId) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
//      //权限校验
//      if (!hrService.userHasPerm(loginUser, "sys:user:delete")) {
//        return response.setFailed(1, "没有访问权限");
//      }
      HrUserEmp user = tokenUtils.getUserInfo();
      if (user == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(user, "sys:user:delete")) {
        return response.setFailed(1, "没有访问权限");
      }

      //删除人员的全部角色
      roleMenuService.userClearRoles(userId);
      //hrUserEmpMapper.deleteById(userId);
      return response.setSuccess("OK");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }

  /**
   * 查看下属单位列表 (树状模式)
   * 权限: sys:user:list
   *
   * @param newDeptNo 单位编号, 如果为空，表示所有单位
   * @return 单位列表
   */
  @RequestMapping("/sys/subdept/tree")
  public JsonResponse subDeptTree(String newDeptNo) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:user:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      response.put("departments", hrService.subDeptTree(newDeptNo));
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }

  /**
   * 添加本地用户， 带角色
   * 权限: sys:user:add
   */
  @RequestMapping("/sys/user/addUserWithRoles")
  @Transactional(rollbackFor = Exception.class)
  public JsonResponse addUsersWithRoles(@RequestBody String json) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:user:add")) {
        return response.setFailed(1, "没有访问权限");
      }

      JSONObject jsonObj = JSON.parseObject(json);
      JSONArray jsonRoles = jsonObj.getJSONArray("roles");
      HrUserEmp user = jsonObj.getObject("user", HrUserEmp.class);
      List<Integer> intRoles = jsonRoles.toJavaList(Integer.class);
      //添加用户
      if (user.getIsVirtual() == 1) {
        user.setEmpTypeName("正式员工");
        user.setIsVirtual(1);
        user.setIsDel(0);
        user.setPostNo("00000000");
        user.setPassword(null);
      }

      if (hrUserEmpMapper.findByUserId(user.getUserId()) != null) {
        return response.setFailed(3, "此用户已存在");
      }

      if (hrUserEmpMapper.insert(user) == 1) {
        //授权
        for (int role : intRoles) {
          if (!roleMenuService.userAddRole(user.getUserId(), role)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response.setFailed(2, "发生异常错误");
          }
        }
      }

      return response.setSuccess("OK");
    } catch (DuplicateKeyException dup) {
      return response.setFailed(3, "此用户已存在");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return response.setFailed(4, e.getLocalizedMessage());
    }
  }

  /**
   * 管理员修改虚拟用户
   * 权限: sys:user:edit
   * 调用方式: Form Post
   * 返回方式：Json
   */
  @PostMapping("/sys/user/modifyVirtualUser")
  @Transactional(rollbackFor = Exception.class)
  public JsonResponse modifyVirtualUser(@RequestBody String json) {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:user:add")) {
        return response.setFailed(1, "没有访问权限");
      }

      JSONObject jsonObj = JSON.parseObject(json);
      JSONArray jsonRoles = jsonObj.getJSONArray("roles");
      HrUserEmp user = jsonObj.getObject("user", HrUserEmp.class);
      List<Integer> intRoles = jsonRoles.toJavaList(Integer.class);
      //修改用户
      if (user.getIsVirtual() != 1) {
        return response.setFailed(2, "只能修改虚拟用户");
      }

      String userId = user.getUserId();

      //如下几个字段不修改
      user.setUserId(null);
      user.setNewEmpNo(null);
      user.setPostNo(null);
      user.setIsDel(null);
      user.setEmpTypeName(null);
      user.setIsVirtual(null);
      user.setApplication(null);
      user.setPassword(null);

      if (hrUserEmpMapper.updateById(user) == 1) {
        //授权
        roleMenuService.userClearRoles(userId);
        for (int role : intRoles) {
          if (!roleMenuService.userAddRole(userId, role)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response.setFailed(3, "发生异常错误");
          }
        }
        return response.setSuccess("OK");
      }
      return response.setFailed(4, "修改用户失败");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return response.setFailed(5, e.getLocalizedMessage());
    }
  }


  /**
   * 添加用户时， 加上角色设置
   * 权限: sys:user:add
   */
  @RequestMapping("/sys/user/addUserWithRolesNew")
  @Transactional(rollbackFor = Exception.class)
  public JsonResponse addUsersWithRolesNew(@RequestBody String json) {
    JsonResponse response = new JsonResponse();
    try {
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
//      //权限校验
//      if (!hrService.userHasPerm(loginUser, "sys:user:add")) {
//        return response.setFailed(1, "没有访问权限");
//      }

      JSONObject jsonObj = JSON.parseObject(json);
      JSONArray jsonRoles = jsonObj.getJSONArray("roles");
      String userId = jsonObj.getString("userId");
     // HrUserEmp user = jsonObj.getObject("user", HrUserEmp.class);
      List<Integer> intRoles = jsonRoles.toJavaList(Integer.class);


        //授权
        for (int role : intRoles) {
          if (!roleMenuService.userAddRole(userId, role)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return response.setFailed(2, "发生异常错误");
          }
        }

      return response.setSuccess("OK");
    } catch (DuplicateKeyException dup) {
      return response.setFailed(3, "此用户已存在");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return response.setFailed(4, e.getLocalizedMessage());
    }
  }





}
