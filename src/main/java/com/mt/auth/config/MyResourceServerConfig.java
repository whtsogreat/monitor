package com.mt.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class MyResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {

    //放开所有请求，由各个API自己决定是否需要token
    http
        .authorizeRequests()
        .anyRequest()
        .permitAll()
        .and()
        .csrf().disable();

  }

}
