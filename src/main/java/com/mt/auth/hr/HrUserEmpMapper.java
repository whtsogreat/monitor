package com.mt.auth.hr;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface HrUserEmpMapper extends BaseMapper<HrUserEmp> {

  /**
   * 根据用户编号加载用户
   *
   * @param userId 用户编号
   * @return 用户entity
   */
  @Select("select u.USER_ID,u.EMP_NO,u.NEW_EMP_NO,u.DEPT_NO,u.NEW_DEPT_NO,u.COMP_NO,u.EMP_NAME,u.ID_CODE,u.E_STEP,u.POST_NO,u.POST_LEVEL,u.F_GENDER as gender,u.F_BIRTHDAY as birthday,u.F_CELL as cellPhone,u.F_POLITICAL as political,u.F_FOLK as folk,u.F_EMP_TYPE_NAME as empTypeName,u.IS_DEL,u.LOGIN_NAME,u.PASSWORD,u.IS_VIRTUAL,u.APPLICATION,c.COMP_NAME,d.DEPT_NAME,p.POST_NAME from hr_user_emp u LEFT JOIN hr_org_comp c on c.COMP_NO=u.COMP_NO LEFT JOIN hr_org_dept d on d.NEW_DEPT_NO=u.NEW_DEPT_NO LEFT JOIN hr_org_post p on p.POST_NO=u.POST_NO WHERE u.USER_ID=#{userId} AND u.IS_DEL=0 AND u.F_EMP_TYPE_NAME='正式员工' AND d.IS_DEL='0'")
  HrUserEmp findByUserId(@Param("userId") String userId);
  /**
   * 根据新用户编号加载用户
   *
   * @param newEmpNo 用户编号
   * @return 用户entity
   */
  @Select("select * from  hr_user_emp u  WHERE u.NEW_EMP_NO=#{newEmpNo} ")
  HrUserEmp findByNewEmpNo(@Param("newEmpNo") String newEmpNo);
  /**
   * 查找newDeptNo的完整路径
   * @param param 调用参数
   */
  @Select("{call fullDeptName(#{newDeptNo,jdbcType=VARCHAR,mode=IN}, #{fullName,jdbcType=VARCHAR,mode=OUT})}")
  @Options(statementType = StatementType.CALLABLE)
  void getFullDeptPath(ProcedureParam param);

  /**
   * 根据登录名加载用户
   *
   * @param loginName 登录账号
   * @return 用户entity
   */
  @Select("select u.USER_ID,u.EMP_NO,u.NEW_EMP_NO,u.DEPT_NO,u.NEW_DEPT_NO,u.COMP_NO,u.EMP_NAME,u.ID_CODE,u.E_STEP,u.POST_NO,u.POST_LEVEL,u.F_GENDER as gender,u.F_BIRTHDAY as birthday,u.F_CELL as cellPhone,u.F_POLITICAL as political,u.F_FOLK as folk,u.F_EMP_TYPE_NAME as empTypeName,u.IS_DEL,u.LOGIN_NAME,u.PASSWORD,u.IS_VIRTUAL,u.APPLICATION,c.COMP_NAME,d.DEPT_NAME,p.POST_NAME from hr_user_emp u LEFT JOIN hr_org_comp c on c.COMP_NO=u.COMP_NO LEFT JOIN hr_org_dept d on d.NEW_DEPT_NO=u.NEW_DEPT_NO LEFT JOIN hr_org_post p on p.POST_NO=u.POST_NO WHERE u.LOGIN_NAME=#{loginName} AND u.IS_DEL=0 AND u.F_EMP_TYPE_NAME='正式员工'AND d.is_del='0'")
  HrUserEmp findByLoginName(@Param("loginName") String loginName);

  /**
   * 分页加载所有用户
   *
   * @param page 分页对象
   * @return 用户列表
   */
  @Select("select u.USER_ID,u.EMP_NO,u.NEW_EMP_NO,u.DEPT_NO,u.NEW_DEPT_NO,u.COMP_NO,u.EMP_NAME,u.ID_CODE,u.E_STEP,u.POST_NO,u.POST_LEVEL,u.F_GENDER as gender,u.F_BIRTHDAY as birthday,u.F_CELL as cellPhone,u.F_POLITICAL as political,u.F_FOLK as folk,u.F_EMP_TYPE_NAME as empTypeName,u.IS_DEL,u.LOGIN_NAME,u.PASSWORD,u.IS_VIRTUAL,u.APPLICATION,c.COMP_NAME,d.DEPT_NAME,p.POST_NAME from hr_user_emp u LEFT JOIN hr_org_comp c on c.COMP_NO=u.COMP_NO LEFT JOIN hr_org_dept d on d.NEW_DEPT_NO=u.NEW_DEPT_NO LEFT JOIN hr_org_post p on p.POST_NO=u.POST_NO WHERE u.IS_DEL=0 AND u.F_EMP_TYPE_NAME='正式员工' AND d.is_del='0'")
  IPage<HrUserEmp> selectUserPage(Page page);

  /**
   * 根据条件查询用户, 分页显示
   *
   * @param page 分页对象
   * @param cond 条件
   * @return 用户列表
   */
  @Select("select u.USER_ID,u.POST_LEVEL,u.EMP_NO,u.NEW_EMP_NO,u.DEPT_NO,u.NEW_DEPT_NO,u.COMP_NO,u.EMP_NAME,u.ID_CODE,u.E_STEP,u.POST_NO,u.POST_LEVEL,u.F_GENDER as gender,u.F_BIRTHDAY as birthday,u.F_CELL as cellPhone,u.F_POLITICAL as political,u.F_FOLK as folk,u.F_EMP_TYPE_NAME as empTypeName,u.IS_DEL,u.LOGIN_NAME,u.PASSWORD,u.IS_VIRTUAL,u.APPLICATION,c.COMP_NAME,d.DEPT_NAME,p.POST_NAME from hr_user_emp u LEFT JOIN hr_org_comp c on c.COMP_NO=u.COMP_NO LEFT JOIN hr_org_dept d on d.NEW_DEPT_NO=u.NEW_DEPT_NO LEFT JOIN hr_org_post p on p.POST_NO=u.POST_NO where u.IS_DEL=0 AND u.F_EMP_TYPE_NAME='正式员工' AND d.is_del='0' ${cond}")
  IPage<HrUserEmp> selectUserCondPage(Page page, @Param("cond") String cond);

  /**
   * 根据条件查询用户
   *
   * @param cond 条件
   * @return 用户列表
   */
  @Select("select u.USER_ID,u.EMP_NO,u.NEW_EMP_NO,u.DEPT_NO,u.NEW_DEPT_NO,u.COMP_NO,u.EMP_NAME,u.ID_CODE,u.E_STEP,u.POST_NO,u.POST_LEVEL,u.F_GENDER as gender,u.F_BIRTHDAY as birthday,u.F_CELL as cellPhone,u.F_POLITICAL as political,u.F_FOLK as folk,u.F_EMP_TYPE_NAME as empTypeName,u.IS_DEL,u.LOGIN_NAME,u.PASSWORD,u.IS_VIRTUAL,u.APPLICATION,c.COMP_NAME,d.DEPT_NAME,p.POST_NAME from hr_user_emp u LEFT JOIN hr_org_comp c on c.COMP_NO=u.COMP_NO LEFT JOIN hr_org_dept d on d.NEW_DEPT_NO=u.NEW_DEPT_NO LEFT JOIN hr_org_post p on p.POST_NO=u.POST_NO where u.IS_DEL=0 AND u.F_EMP_TYPE_NAME='正式员工'AND d.is_del='0' ${cond}")
  List<HrUserEmp> selectUserCond(@Param("cond") String cond);

  /**
   * 分页 查询某单位下的全部员工
   *
   * @param page   分页对象
   * @param newDeptNo 单位编号
   * @return 用户列表
   */
  @Select("select u.USER_ID,u.EMP_NO,u.NEW_EMP_NO,u.DEPT_NO,u.NEW_DEPT_NO,u.COMP_NO,u.EMP_NAME,u.ID_CODE,u.E_STEP,u.POST_NO,u.POST_LEVEL,u.F_GENDER as gender,u.F_BIRTHDAY as birthday,u.F_CELL as cellPhone,u.F_POLITICAL as political,u.F_FOLK as folk,u.F_EMP_TYPE_NAME as empTypeName,u.IS_DEL,u.LOGIN_NAME,u.PASSWORD,u.IS_VIRTUAL,u.APPLICATION,c.COMP_NAME,d.DEPT_NAME,p.POST_NAME from hr_user_emp u LEFT JOIN hr_org_comp c on c.COMP_NO=u.COMP_NO LEFT JOIN hr_org_dept d on d.NEW_DEPT_NO=u.NEW_DEPT_NO LEFT JOIN hr_org_post p on p.POST_NO=u.POST_NO where u.IS_DEL=0 AND u.F_EMP_TYPE_NAME='正式员工' AND d.is_del='0' and u.NEW_DEPT_NO like concat(#{newDeptNo},'%')")
  IPage<HrUserEmp> selectUserDeptPage(Page page, @Param("newDeptNo") String newDeptNo);


  /**
   * 查询某单位下的全部员工
   *
   * @param newDeptNo 单位编号
   * @return 用户列表
   */
  @Select("select u.USER_ID,u.EMP_NO,u.NEW_EMP_NO,u.DEPT_NO,u.NEW_DEPT_NO,u.COMP_NO,u.EMP_NAME,u.ID_CODE,u.E_STEP,u.POST_NO,u.POST_LEVEL,u.F_GENDER as gender,u.F_BIRTHDAY as birthday,u.F_CELL as cellPhone,u.F_POLITICAL as political,u.F_FOLK as folk,u.F_EMP_TYPE_NAME as empTypeName,u.IS_DEL,u.LOGIN_NAME,u.PASSWORD,u.IS_VIRTUAL,u.APPLICATION,c.COMP_NAME,d.DEPT_NAME,p.POST_NAME from hr_user_emp u LEFT JOIN hr_org_comp c on c.COMP_NO=u.COMP_NO LEFT JOIN hr_org_dept d on d.NEW_DEPT_NO=u.NEW_DEPT_NO LEFT JOIN hr_org_post p on p.POST_NO=u.POST_NO where u.IS_DEL=0 AND u.F_EMP_TYPE_NAME='正式员工'AND d.is_del='0' and u.NEW_DEPT_NO like concat(#{newDeptNo},'%')")
  List<HrUserEmp> selectUserDept(@Param("newDeptNo") String newDeptNo);

  /**
   * 更新用户密码
   *
   * @param userId   用户编号
   * @param password 新密码
   * @return 是否成功 (1)
   */
  @Update("update hr_user_emp set PASSWORD=#{password} where USER_ID=#{userId}")
  int updatePassword(@Param("userId") String userId, @Param("password") String password);
}
