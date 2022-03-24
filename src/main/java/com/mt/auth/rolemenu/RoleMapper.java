package com.mt.auth.rolemenu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleMapper extends BaseMapper<Role> {

  /**
   * 加载role
   *
   * @param id 角色ID
   * @return Role
   */
  @Select("SELECT * from t_role where ROLE_ID=#{id}")
  Role getRole(@Param("id") int id);

  /**
   * 查询role下的所有menus
   * 平面模式
   *
   * @param roleId 角色id
   * @return 菜单列表
   */
  @Select("SELECT m.* from t_menu m where m.MENU_ID in ( SELECT DISTINCT r.MENU_ID from t_role_menu r where r.ROLE_ID=#{roleId})")
  List<Menu> getRoleMenus(@Param("roleId") int roleId);

  /**
   * 获取所有角色
   *
   * @return Role列表
   */
  @Select("SELECT * from t_role")
  List<Role> getAllRoles();

  /**
   * 新增Role
   *
   * @param role Role对象
   * @return 1: 成功
   */
  @Insert({"insert into t_role(ROLE_NAME, REMARK) values(#{roleName}, #{remark})"})
  @Options(useGeneratedKeys = true, keyProperty = "roleId")
  int insertRole(Role role);

  /**
   * 清除role全部menus
   *
   * @param roleId 角色ID
   */
  @Delete("DELETE from t_role_menu where ROLE_ID=#{roleId}")
  void clearRoleMenus(@Param("roleId") int roleId);

  /**
   * 新增一个role menu
   *
   * @param roleId 角色ID
   * @param menuId 菜单ID
   * @return 影响的行数
   */
  @Insert("INSERT into t_role_menu (ROLE_ID, MENU_ID) values (#{roleId}, #{menuId})")
  int addRoleMenu(@Param("roleId") int roleId, @Param("menuId") int menuId);

  /**
   * 查询用户角色
   *
   * @param userId 用户编号
   * @return 角色ID列表
   */
  @Select("SELECT DISTINCT r.* from t_user_role ur left join t_role r on ur.ROLE_ID=r.ROLE_ID where ur.USER_ID=#{userId}")
  List<Role> getUserRoles(@Param("userId") String userId);

  /**
   * 用户新增一个权限
   *
   * @param userId 用户编号
   * @param roleId 角色ID
   * @return 影响的行数
   */
  @Insert("INSERT into t_user_role (USER_ID, ROLE_ID) values (#{userId}, #{roleId})")
  int addUserRole(@Param("userId") String userId, @Param("roleId") int roleId);

  /**
   * 删除用户的一个权限
   *
   * @param userId 用户编号
   * @param roleId 角色ID
   * @return 影响的行数
   */
  @Delete("DELETE from t_user_role where USER_ID=#{userId} and ROLE_ID=#{roleId}")
  int removeUserRole(@Param("userId") String userId, @Param("roleId") int roleId);

  /**
   * 删除user的全部role
   *
   * @param userId 用户编号
   */
  @Delete("DELETE from t_user_role where USER_ID=#{userId}")
  void removeUserAllRole(@Param("userId") String userId);

  /**
   * 删除role的全部user
   *
   * @param roleId 角色编号
   */
  @Delete("DELETE from t_user_role where ROLE_ID=#{roleId}")
  void removeRoleAllUser(@Param("roleId") Integer roleId);




}
