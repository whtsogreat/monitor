package com.mt.auth.hr;


import lombok.Data;

/**
 * 用于调用mysql 存储过程
 */

@Data
public class ProcedureParam {
  private String newDeptNo;
  private String fullName;
}
