package com.mt.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 人员菜单管理controller
 * @Author qingrui
 * @Date 2021/3/8 10:38
 * @Version 1.0
 */
@RestController
@RequiredArgsConstructor
public class UserMenuController {
    private final @NonNull LoginUserService loginUserService;
    private final @NonNull HrService hrService;
    private final @NonNull TokenUtils tokenUtils;
    private final @NonNull UserMenuMapper userMenuMapper;

    /**
     * 新增用户的菜单
     * 权限: sys:user:edit
     *
     * @param userId 用户id
     */
    @RequestMapping("/sys/user/setMenu")
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse userMenuAdd(String userId, String menuIdListsA) {
        JsonResponse response = new JsonResponse();
        try {
//      if (role == null) {
//        return response.setFailed(1, "参数不正确");
//      }
//      HrUserEmp loginUser = loginUserService.checkLoginUser(response, false);
//      if (loginUser == null) {
//        return response;
//      }
            //权限校验
//      if (!hrService.userHasPerm(loginUser, "sys:role:add")) {
//        return response.setFailed(2, "没有访问权限");
//      }
            HrUserEmp user = tokenUtils.getUserInfo();
            if(user == null){
                return response;
            }
            if (!hrService.userHasPerm(user, "sys:user:edit")) {
                return response.setFailed(2, "没有访问权限");
            }

            List<Menu> userMenus = userMenuMapper.getUserMenus(userId);
            if (userMenus == null) {
                userMenus = new ArrayList<>();
            }
            String[] menuIds = menuIdListsA.split(",");
            int id;
            for (String sId : menuIds) {
                try {
                    id = Integer.parseInt(sId);
                } catch (Exception e) {
                    continue;
                }
                boolean found = false;
                for (Menu menu : userMenus) {
                    if (menu.getMenuId() == id) {
                        found = true;
                        //标记已经处理了
                        menu.setMenuId(0);
                        break;
                    }
                }
                if (found) {
                    continue;
                }
                UserMenu userMenu = new UserMenu();
                userMenu.setMenuId(id);
                userMenu.setUserId(userId);
                //新的授权
                if (userMenuMapper.insert(userMenu) ==0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return response.setFailed(3, "发生异常错误");
                }
            }

            //查找已经被删除的授权
            for (Menu userMenu : userMenus) {
                if (userMenu.getMenuId() != 0) {
                    //循环删除
                    if(userMenuMapper.removeUserMenu(userId,userMenu.getMenuId()) !=1){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return response.setFailed(4, "发生异常错误");
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
     * 根据用户userId加载menu
     * 不加载role menus
     * 权限: sys:user:edit
     *
     * @param userId 用户ID
     */
    @GetMapping("/sys/userMenu/info/{userId}")
    public JsonResponse getUserMenu(@PathVariable("userId") String userId) {
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

            List<Menu> userMenus = userMenuMapper.getUserMenus(userId);


//
//      List<Menu> menus = roleMenuService.getRoleMenus(role);
            response.put("menuIdList", calcMenuIdList(userMenus));

            return response.setSuccess("OK");
        } catch (Exception e) {
            return response.setFailed(4, e.getLocalizedMessage());
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
}
