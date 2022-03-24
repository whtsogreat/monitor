package com.mt.auth.utils;

import com.mt.auth.hr.HrService;
import com.mt.auth.hr.HrUserEmp;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LoginUserService {

  private final @NonNull TokenUtils tokenUtils;
  private final @NonNull HrService hrService;

  /**
   * 获取登录用户
   * 注： 通常配合下面的 checkLoginUser 来使用
   *
   * @param returnUser   要返回的用户
   * @param fullDeptName 是否加载完整的 部门路径
   * @return 0: 成功(本地用户和token用户完全一样)
   * 0: 成功(本地用户和token用户略微不一样， 已用token用户替换了本地用户属性)
   * 1: 失败(本地用户和token用户有较大区别, UserId,EmpNo,deptNo,compNo,empName,gender,empTypeName,isDel)
   * 2: 失败(没有token用户,需要登录)
   * 3: 失败(本地用户不存在,未授权登录)
   */
  public int getLoginUser(HrUserEmp returnUser, boolean fullDeptName) {
    HrUserEmp tokenUser = tokenUtils.getUserInfo();
    if (tokenUser == null || StringUtils.isEmpty(tokenUser.getUserId())) {
      return 2;
    }
    HrUserEmp user = new HrUserEmp();
//    HrUserEmp user = hrService.loadUser(tokenUser.getUserId());
//    if (user == null || user.getUserId() == null) {
//      return 3;
//    }
//    switch (user.compareTo(tokenUser)) {
//      //略微不同
//      case 1:
//        //重新将token User保存到本地 (本地有可能有手机号， 忽略手机号)
//        BeanUtils.copyProperties(tokenUser, user, "cellPhone");
//        hrService.updateUser(user);
//        break;
//      //很大不同
//      case 2:
//        return 1;
//      //tokenUser不正确
//      case 3:
//        return 2;
//      default:
//        break;
//    }
//    if (fullDeptName && user.getNewDeptNo() != null) {
//      user.setFullDeptName(user.getCompName() + hrService.getFullDeptName(user.getNewDeptNo()));
//    }
//    //处理特殊的岗位
//    if ("00000000".equals(user.getPostNo())) {
//      user.setPostName("");
//    }
    BeanUtils.copyProperties(tokenUser, returnUser);
    return 0;
  }

  /**
   * 检查用户登录情况
   *
   * @param response     用于返回错误信息
   * @param fullDeptName 是否加载完整单位路径
   * @return 登录用户对象, null 表示失败，失败原因在response里
   */
  public HrUserEmp checkLoginUser(JsonResponse response, boolean fullDeptName) {
    HrUserEmp user = new HrUserEmp();
    int loginStatus = getLoginUser(user, fullDeptName);
    switch (loginStatus) {
      case 1:
        response.setFailed(100, "您的用户信息不正确, 请联系管理员处理");
        return null;
      case 2:
        response.setFailed(101, "请登录");
        return null;
      case 3:
        response.setFailed(102, "没有授权访问");
        return null;
      default:
        return user;
    }
  }

}
