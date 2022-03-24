package com.mt.auth.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 部门表主要字段
 */

@Data
@TableName("hr_org_dept")
public class HrOrgDept implements Serializable {

  /**
   * 部门ID
   */
  @TableId(value = "DEPT_ID", type = IdType.UUID)
  private String deptId;
  /**
   * 部门编号
   */
  @TableId(value = "DEPT_NO", type = IdType.INPUT)
  private String deptNo;
  /**
   * 部门名称
   */
  @TableField("DEPT_NAME")
  private String deptName;


  /**
   * 上级部门编号
   */
  @TableField("P_DEPT_NO")
  private String pDeptNo;
  /**
   * 上级部门编号
   */
  @TableField("P_DEPT_ID")
  private String pDeptId;


  /**
   * 部门等级，部门等级划分(顶层为1)
   */
  @TableField("LEVEL")
  private Integer level;

  /**
   * 是否删除，0：未删除，1：已删除
   */
  @TableField("IS_DEL")
  private Integer isDel;

  /**
   * 部门级别排序，前台部门显示序号(第一个为1)
   */
  @TableField("F_ORDER")
  private Integer fOrder;

  /**
   * 下级单位列表
   */
  @TableField(exist = false)
  List<HrOrgDept> child;


  /**
   * 下级单位列表
   */
  @TableField(exist = false)
  List<HrOrgDept> children;

  /**
   * 给树状图用，防止循环
   */
  @TableField(exist = false)
  private boolean treeAdded;


  /**
   * 给级联使用，值
   */
  @TableField(exist = false)
  private String value;
  /**
   * 给级联使用，文本
   */
  @TableField(exist = false)
  private String label;

}
