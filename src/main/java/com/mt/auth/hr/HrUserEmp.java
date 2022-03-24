package com.mt.auth.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mt.auth.rolemenu.Role;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 员工表
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("hr_user_emp")
public class HrUserEmp implements Serializable {

  @TableField("USER_ID")
  private String userId;

  /**
   * 人员编号
   */
  @TableId(value = "EMP_NO", type = IdType.INPUT)
  private String empNo;

  /**
   * 新人员编号
   */
  @TableField("NEW_EMP_NO")
  private String newEmpNo;

  /**
   * 部门编号，直属部门编号
   */
  @TableField("DEPT_NO")
  private String deptNo;

  /**
   * 新部门编号，直属部门编号
   */
  @TableField("NEW_DEPT_NO")
  private String newDeptNo;

  /**
   * 部门名称
   */
  @TableField(exist = false)
  private String deptName;

  /**
   * 完整部门名称
   */
  @TableField(exist = false)
  protected String fullDeptName;

  /**
   * 公司编号，直属公司编号
   */
  @TableField("COMP_NO")
  private String compNo;

  /**
   * 公司名称
   */
  @TableField(exist = false)
  private String compName;

  /**
   * 人员名称
   */
  @TableField("EMP_NAME")
  private String empName;

  /**
   * 人员身份证
   */
  @TableField("ID_CODE")
  private String idCode;

  /**
   * 学历
   */
  @TableField("E_STEP")
  private String eStep;

  /**
   * 岗位编号
   */
  @TableField("POST_NO")
  private String postNo;

  /**
   * 岗位编号
   */
  @TableField(exist = false)
  private String postName;

  /**
   * 岗位级别
   */
  @TableField("POST_LEVEL")
  private String postLevel;

  /**
   * 性别，1：男，2：女
   */
  @TableField("F_GENDER")
  private Integer gender;

  /**
   * 出生日期
   */
  @TableField("F_BIRTHDAY")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date birthday;

  /**
   * 手机号码
   */
  @TableField("F_CELL")
  private String cellPhone;

  /**
   * 政治面貌
   */
  @TableField("F_POLITICAL")
  private String political;

  /**
   * 民族
   */
  @TableField("F_FOLK")
  private String folk;

  /**
   * 员工状态: 退休，正式员工，外勤。。。
   */
  @TableField("F_EMP_TYPE_NAME")
  private String empTypeName;

  /**
   * 是否删除，0：未删除，1：已删除
   */
  @TableField("IS_DEL")
  private Integer isDel;

  /**
   * 登录账号
   */
  @TableField("LOGIN_NAME")
  private String loginName;

  /**
   * 登录密码
   */
  @TableField("PASSWORD")
  private String password;

  /**
   * 是否虚拟用户
   */
  @TableField("IS_VIRTUAL")
  private Integer isVirtual;

  /**
   * 虚拟用户的client_id
   */
  @TableField("APPLICATION")
  private String application;

  /**
   * 用户的角色列表
   */
  @TableField(exist = false)
  private List<Role> roles;
  /**
   * 用户专员类别
   */
  @TableField(exist = false)
  private String commissioners;
  /**
   * 用户专员中文
   */
  @TableField(exist = false)
  private String commissionersChinese;

  /**
   * 比较两个用户
   *
   * @param user 待比较的用户
   * @return 0:完全一样 1:几乎一样 2:重要信息不一致 3:用户信息不正确
   */
  public int compareTo(HrUserEmp user) {
    if (user == null || user.getUserId() == null || user.getEmpNo() == null ||
        user.getEmpName() == null || user.getEmpTypeName() == null || user.getIsDel() == null) {
      return 3;
    }
    if (!user.getUserId().equals(this.getUserId())) {
      return 2;
    }
    if (!user.getEmpNo().equals(this.getEmpNo())) {
      return 2;
    }
    if (!user.getEmpName().equals(this.getEmpName())) {
      return 2;
    }
    if (!user.getEmpTypeName().equals(this.getEmpTypeName())) {
      return 2;
    }
    if (this.getIsDel() != null) {
      if (this.getIsDel().intValue() != user.getIsDel().intValue()) {
        return 2;
      }
    }

    if (!StringUtils.isEmpty(this.getCompNo())) {
      if (!this.getCompNo().equals(user.getCompNo())) {
        return 2;
      }
    }
    //隐患评估人部门修改后不能登录需注释这段代码屏蔽部门编号比较
   /* if (!StringUtils.isEmpty(this.getNewDeptNo())) {
      if (!user.getNewDeptNo().equals(this.getNewDeptNo())) {
        return 2;
      }
    }*/

   /* if (!StringUtils.isEmpty(this.getDeptNo())) {
      if (!this.getDeptNo().equals(user.getDeptNo())) {
        return 2;
      }
    }*/
    if (this.getGender() != null) {
      if (user.getGender() == null) {
        return 2;
      }
      if (this.getGender().intValue() != user.getGender().intValue()) {
        return 2;
      }
    }

    if (!StringUtils.isEmpty(this.getNewEmpNo())) {
      if (!user.getNewEmpNo().equals(this.getNewEmpNo())) {
        return 1;
      }
    }

    if (!StringUtils.isEmpty(this.getIdCode())) {
      if (!user.getIdCode().equals(this.getIdCode())) {
        return 1;
      }
    }

    if (!StringUtils.isEmpty(this.getEStep())) {
      if (!user.getEStep().equals(this.getEStep())) {
        return 1;
      }
    }

    if (!StringUtils.isEmpty(this.getPostNo())) {
      if (!user.getPostNo().equals(this.getPostNo())) {
        return 1;
      }
    }

    if (!StringUtils.isEmpty(this.getPostLevel())) {
      if (!user.getPostLevel().equals(this.getPostLevel())) {
        return 1;
      }
    }

    if (user.getBirthday() != null) {
      if (!user.getBirthday().equals(this.getBirthday())) {
        return 1;
      }
    }

    if (!StringUtils.isEmpty(this.getCellPhone())) {
      if (!user.getCellPhone().equals(this.getCellPhone())) {
        return 1;
      }
    }

    if (!StringUtils.isEmpty(this.getPolitical())) {
      if (!user.getPolitical().equals(this.getPolitical())) {
        return 1;
      }
    }

    if (!StringUtils.isEmpty(this.getFolk())) {
      if (!user.getFolk().equals(this.getFolk())) {
        return 1;
      }
    }

    if (user.getIsVirtual() != null) {
      if (this.getIsVirtual() == null) {
        return 1;
      }
      if (user.getIsVirtual().intValue() != this.getIsVirtual().intValue()) {
        return 1;
      }
    }

    return 0;
  }

}
