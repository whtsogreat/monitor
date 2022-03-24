package com.mt.auth.portal;

import com.alibaba.fastjson.JSONObject;
import com.mt.auth.utils.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

/**
 * 门户网站授权用
 */

@RestController
@RequiredArgsConstructor
public class Portal {

  /*
   * 门户网站统一登录鉴权
   * 发布前，请确保以下常量配置正确
   */

  /**
   * oauth client id
   */
  private static final String CLIENT_ID = "mtcloud";
  /**
   * 本应用在北京地铁里的appId
   */
  private static final int PORTAL_ID = 23;
  /**
   * 本应用在北京地铁里的appKey
   */
  private static final String PORTAL_KEY = "8888888888888888";
  /**
   * 本应用在地铁科技里的appId
   */
  private static final int MT_PORTAL_ID = 23;
  /**
   * 本应用在地铁科技里的appKey
   */
  private static final String MT_PORTAL_KEY = "8888888888888888";
  /**
   * 授权服务器地址
   */
  private static final String AUTH_SERVER = "http://localhost:8100/auth";

  /**
   * 北京地铁集团门户认证
   * 认证入口
   * http://localhost:8081/portal?empno=124000001&t_time=1505113555&token_23=7e196d9c183f1e0e0aa7508df3449791
   *
   * @return JSON
   */
  @RequestMapping("/portalValidate")
  public JsonResponse portalValidate(String empNo, Long time, String token, Integer portalId) {
    JsonResponse response = new JsonResponse();
    if (empNo == null || time == null || token == null || portalId == null || portalId != PORTAL_ID) {
      return response.setFailed(1, "非法请求");
    }
    String md5 = DigestUtils.md5Hex(empNo + PORTAL_KEY + time);
    boolean valid = md5.equalsIgnoreCase(token);
    if (valid) {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      int day = cal.get(Calendar.DAY_OF_MONTH);
      String checkCode = DigestUtils.md5Hex(empNo + year + "" + month + "" + day);

      RestTemplate restTemplate = new RestTemplate();
      String url = AUTH_SERVER + "/createTokenByEmpNo?clientId=" + CLIENT_ID + "&empNo=" + empNo + "&check_code=" + checkCode;
      String json = restTemplate.getForObject(url, String.class);
      JSONObject jsonObject = JSONObject.parseObject(json);
      Integer code = jsonObject.getInteger("code");
      if (code == null || code != 0) {
        return response.setFailed(2, jsonObject.getString("message"));
      }
      response.putAll(jsonObject);
      return response.setSuccess("OK");
    }
    return response.setFailed(3, "校验失败");
  }

  /**
   * 地铁科技门户认证
   * 采用的和集团认证方式完全一样, portalId 和 key 可能不一样
   * http://xxxx/mtportal?empno=61002440&t_time=1505113555&token_23=3ee5ed786ededf3f24489bbbcd541b4d
   *
   * @return JSON
   */
  @RequestMapping("/mtPortalValidate")
  public JsonResponse mtPortalValidate(String empNo, Long time, String token, Integer portalId) {
    JsonResponse response = new JsonResponse();
    if (empNo == null || time == null || token == null || portalId == null || portalId != MT_PORTAL_ID) {
      return response.setFailed(1, "非法请求");
    }
    String md5 = DigestUtils.md5Hex(empNo + MT_PORTAL_KEY + time);
    boolean valid = md5.equalsIgnoreCase(token);
    if (valid) {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1;
      int day = cal.get(Calendar.DAY_OF_MONTH);
      String checkCode = DigestUtils.md5Hex(empNo + year + "" + month + "" + day);

      RestTemplate restTemplate = new RestTemplate();
      String url = AUTH_SERVER + "/createTokenByEmpNo?clientId=" + CLIENT_ID + "&empNo=" + empNo + "&check_code=" + checkCode;
      String json = restTemplate.getForObject(url, String.class);
      JSONObject jsonObject = JSONObject.parseObject(json);
      Integer code = jsonObject.getInteger("code");
      if (code == null || code != 0) {
        return response.setFailed(2, jsonObject.getString("message"));
      }
      response.putAll(jsonObject);
      return response.setSuccess("OK");
    }
    return response.setFailed(3, "校验失败");
  }

}
