package com.mt.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mt.auth.hr.HrUserEmp;
import com.mt.auth.utils.JsonResponse;
import com.mt.auth.utils.TokenUtils;
import com.mt.sys.dao.SysLogDao;
import com.mt.sys.entity.SysLogEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 系统日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/log")
public class SysLogController {

	private final @NonNull SysLogDao sysLogDao;

	private final @NonNull TokenUtils tokenUtils;//引入登录用户权限类
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public JsonResponse list(@RequestParam Map<String, Object> params,Integer pageNo, Integer pageSize){


		JsonResponse jsonResponse = new JsonResponse();
		try {
			//检查登录用户权限
			HrUserEmp user = tokenUtils.getUserInfo();
			if (user == null) {
				return jsonResponse.setFailed(2,"失败");
			}
			if(pageNo == null)  pageNo=1;
			if(pageSize== null) pageSize=10;
			Page<SysLogEntity> page = new Page<>(pageNo, pageSize);
			jsonResponse.put("page",sysLogDao.selectPage(page,null));
			return jsonResponse.setSuccess("OK");
		} catch (Exception e) {
			return jsonResponse.setFailed(2,e.getLocalizedMessage());
		}

	}
	
}
