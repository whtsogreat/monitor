package com.mt.demo2.mapper;

import com.mt.demo2.entity.RiskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: wss
 * @Date: 15:20 2021/7/1
 * @Description: 解析excel
 */
@Mapper
public interface RiskMapper {
    int insertRiskVO(@Param("riskVO") RiskVO riskVO);


    Map<String,String> selectGenderCode(@Param("gender") String gender);

}
