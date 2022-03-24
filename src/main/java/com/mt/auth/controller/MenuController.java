package com.mt.auth.controller;

import com.mt.auth.hr.HrService;
import com.mt.auth.hr.HrUserEmp;
import com.mt.auth.rolemenu.Menu;
import com.mt.auth.rolemenu.MenuMapper;
import com.mt.auth.rolemenu.MenuTree;
import com.mt.auth.rolemenu.RoleMenuService;
import com.mt.auth.utils.JsonResponse;
import com.mt.auth.utils.LoginUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理
 * 后台管理用
 */

@RestController
@RequiredArgsConstructor
public class MenuController {

  private final @NonNull LoginUserService loginUserService;
  private final @NonNull HrService hrService;
  private final @NonNull RoleMenuService roleMenuService;
  private final @NonNull MenuMapper menuMapper;

  /**
   * 加载全部菜单和功能 (有效的菜单或功能)
   * 树状模式
   * 权限: sys:menu:list
   * 用于给角色授权
   */
  @GetMapping("/sys/menu/tree")
  public JsonResponse getSysMenuTree() {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:menu:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      response.put("menus", roleMenuService.getMenuTree());
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }

  /**
   * @MethodName list
   * @Description 配合前端VUE使用，使菜单可按照层级折叠
   * @param:
   * @Return com.dtkj.propaganda.modules.auth.utils.JsonResponse
   * @Throws
   * @Author sunlei
   * @Date 2020/7/23 4:06 下午
   */
  @GetMapping("/sys/menu/treeV2")
  public JsonResponse list(){
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:menu:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      List<Menu> menuList = menuMapper.getAllMenus();
      for(Menu menu : menuList){
        Menu parentMenu = menuMapper.selectById(menu.getParentId());
        if(parentMenu != null){
          menu.setParentName(parentMenu.getMenuName());
        }
      }
      response.put("menus", menuList);
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }


  /**
   * 加载全部菜单和功能 (有效的菜单或功能)
   * 平面模式
   * 权限: sys:menu:list
   */
  @GetMapping("/sys/menu/list")
  public JsonResponse getSysMenuList() {
    JsonResponse response = new JsonResponse();
    try {
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:menu:list")) {
        return response.setFailed(1, "没有访问权限");
      }

      List<Menu> all = menuMapper.getAllMenus();
      if (all == null) {
        all = new ArrayList<>();
      }
      List<Menu> result = new ArrayList<>();
      List<Menu> topMenus = MenuTree.filterParent(all, 0);
      for (Menu menu : topMenus) {
        result.add(menu);
        formatChildMenu(all, result, menu);
      }

      response.put("menus", result);
      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(2, e.getLocalizedMessage());
    }
  }

  /**
   * 为前端vue准备menu
   * all: 全部菜单
   * result: 保存到结果菜单
   * menu: 当前需要格式化的菜单
   */
  private void formatChildMenu(List<Menu> all, List<Menu> result, Menu menu) {
    List<Menu> childMenus = MenuTree.filterParent(all, menu.getMenuId());
    for (Menu child : childMenus) {
      child.setParentName(menu.getMenuName());
      result.add(child);
      formatChildMenu(all, result, child);
    }
  }

  /**
   * 新增菜单
   * 权限: sys:menu:add
   *
   * @param menu 菜单
   * @return WebResponse
   */
  @RequestMapping("/sys/menu/add")
  public JsonResponse menuAdd(Menu menu) {
    JsonResponse response = new JsonResponse();
    try {
      if (menu == null) {
        return response.setFailed(1, "参数不正确");
      }
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:menu:add")) {
        return response.setFailed(2, "没有访问权限");
      }

      if (!roleMenuService.addMenu(menu)) {
        return response.setFailed(3, "数据保存失败");
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        return response.setFailed(5, "保存失败，请确保perm唯一");
      }
      return response.setFailed(6, e.getLocalizedMessage());
    }
  }

  /**
   * 修改菜单
   * 权限: sys:menu:edit
   *
   * @param menu 菜单
   */
  @RequestMapping("/sys/menu/edit")
  public JsonResponse menuEdit(Menu menu) {
    JsonResponse response = new JsonResponse();
    try {
      if (menu == null) {
        return response.setFailed(1, "参数不正确");
      }
      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
      if (loginUser == null) {
        return response;
      }
      //权限校验
      if (!hrService.userHasPerm(loginUser, "sys:menu:edit")) {
        return response.setFailed(2, "没有访问权限");
      }

      if (!roleMenuService.updateMenu(menu)) {
        return response.setFailed(3, "数据保存失败");
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      if (e instanceof DuplicateKeyException) {
        return response.setFailed(4, "保存失败，请确保perm唯一");
      }
      return response.setFailed(5, e.getLocalizedMessage());
    }
  }

  /**
   * 删除菜单
   * 权限: sys:menu:delete
   *
   * @param id 菜单ID
   */
  @RequestMapping("/sys/menu/delete/{id}")
  public JsonResponse menuDelete(@PathVariable Integer id) {
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
      if (!hrService.userHasPerm(loginUser, "sys:menu:delete")) {
        return response.setFailed(2, "没有访问权限");
      }

      List<Menu> subMenus = roleMenuService.getSubMenu(id);
      if (!subMenus.isEmpty()) {
        return response.setFailed(3, "请先删除子菜单功能");
      }
      if (!roleMenuService.deleteMenu(id)) {
        return response.setFailed(4, "数据保存失败");
      }

      return response.setSuccess("OK");
    } catch (Exception e) {
      return response.setFailed(5, e.getLocalizedMessage());
    }
  }

  /**
   * 加载菜单
   * 权限: sys:menu:list
   *
   * @param id 菜单/功能ID
   */
  @GetMapping("/sys/menu/info/{id}")
  public JsonResponse getMenu(@PathVariable("id") Integer id) {
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
      if (!hrService.userHasPerm(loginUser, "sys:menu:list")) {
        return response.setFailed(2, "没有访问权限");
      }

      Menu menu = roleMenuService.getMenu(id);
      if (menu == null) {
        return response.setFailed(3, "记录不存在");
      }

      if (menu.getParentId() != 0) {
        List<Menu> all = menuMapper.getAllMenus();
        for (Menu m : all) {
          if (m.getMenuId().intValue() == menu.getParentId()) {
            menu.setParentName(m.getMenuName());
            break;
          }
        }
      }

      response.put("menu", menu);
      return response.setSuccess("OK");

    } catch (Exception e) {
      return response.setFailed(4, e.getLocalizedMessage());
    }
  }
}
