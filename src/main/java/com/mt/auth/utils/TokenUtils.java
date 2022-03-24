package com.mt.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mt.auth.hr.HrUserEmp;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class TokenUtils {

  private final @NonNull HttpServletRequest request;

  private final @NonNull TokenStore tokenStore;

  private OAuth2AccessToken getAccessToken() throws AccessDeniedException {
    BearerTokenExtractor tokenExtractor = new BearerTokenExtractor();
    OAuth2AccessToken token;
    // 抽取token
    Authentication a = tokenExtractor.extract(request);
    if (a == null) {
      return null;
    }
    try {
      // 调用JwtAccessTokenConverter的extractAccessToken方法解析token
      token = tokenStore.readAccessToken((String) a.getPrincipal());
    } catch (Exception e) {
      //throw new AccessDeniedException("AccessToken Not Found.");
      return null;
    }
    return token;
  }

  /**
   * 从token获取用户信息
   */
  public HrUserEmp getUserInfo() {
    OAuth2AccessToken token = getAccessToken();
    if (token == null) {
      return null;
    }

    Map<String, Object> additionalInfo = token.getAdditionalInformation();
    if (additionalInfo == null) {
      return null;
    }
    LinkedHashMap map = (LinkedHashMap) additionalInfo.get("user");
    if (map == null) {
      return null;
    }
    //去除这些 HrUserEmp 没有的字段
    map.remove("password");
    map.remove("username");
    map.remove("authorities");
    map.remove("accountNonExpired");
    map.remove("accountNonLocked");
    map.remove("credentialsNonExpired");
    map.remove("enabled");
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(map, HrUserEmp.class);
  }
}
