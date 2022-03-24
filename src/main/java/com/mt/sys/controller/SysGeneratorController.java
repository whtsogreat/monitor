/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.mt.sys.controller;

import com.alibaba.fastjson.JSON;
import com.mt.auth.utils.JsonResponse;
import com.mt.sys.entity.ColumnEntity;
import com.mt.sys.service.SysGeneratorService;
import com.mt.utils.GenUtils;
import com.mt.utils.PageUtils;
import com.mt.utils.Query;
import com.mt.utils.R;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = sysGeneratorService.queryList(new Query(params));
		
		return R.ok().put("page", pageUtil);
	}
	/**
	 * 修改回显
	 */
	@ResponseBody
	@RequestMapping("/edit")
	public R edit(String tableName){
		List<ColumnEntity> list2 =sysGeneratorService.selectByTableName(tableName);
		if(list2.size()<=0){
			List<Map<String, String>> list = sysGeneratorService.queryColumns(tableName);
			for (Map<String, String> map : list) {
				String attrName = GenUtils.columnToJava(map.get("columnName"));
				map.put("attrName2",StringUtils.uncapitalize(attrName));

				//列的数据类型，转换成Java类型
				String attrType = GenUtils.getConfig().getString(map.get("dataType"), GenUtils.columnToJava(map.get("dataType")));
				map.put("attrType",attrType);
			}
			return R.ok().put("map", list);
		}else{

			return R.ok().put("map", list2);
		}



	}
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	public void code(String tables, HttpServletResponse response) throws IOException{
		byte[] data = sysGeneratorService.generatorCode(tables.split(","));
		
		response.reset();  
        response.setHeader("Content-Disposition", "attachment; filename=\"generator.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");  
  
        IOUtils.write(data, response.getOutputStream());
	}
	/**
	 * 生成代码2
	 */
	@RequestMapping("/code2")
	public void code2(String tableName, HttpServletResponse response) throws IOException{


		byte[] data = sysGeneratorService.generatorCode2(tableName);

		response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+tableName+".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin","*");
        IOUtils.write(data, response.getOutputStream());
	}
	/**
	 * 添加与修改
	 */
	@RequestMapping("/insert")
	@Transactional
	public JsonResponse insert(@RequestParam String tableName, @RequestParam String tables) throws IOException{
		JsonResponse jsonResponse = new JsonResponse();
		List<ColumnEntity> columnEntities1 = sysGeneratorService.selectByTableName(tableName);
		List<ColumnEntity> columnEntities = JSON.parseArray(tables, ColumnEntity.class);
		if(columnEntities1.size()>0){
			sysGeneratorService.deleteByTableName(tableName);
		}
		for (int i = 0; i < columnEntities.size(); i++) {
			columnEntities.get(i).setTableName(tableName);
			columnEntities.get(i).setSort(i+1);
			columnEntities.get(i).setAttrName2(StringUtils.uncapitalize(columnEntities.get(i).getAttrName()));
			sysGeneratorService.insert(columnEntities.get(i));
			System.out.println(columnEntities.get(i));
		}

		return jsonResponse.setSuccess("OK");
	}
}
