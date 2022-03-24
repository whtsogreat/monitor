package com.mt.demo2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableResourceServer

@ComponentScan({"com.mt.auth", "com.mt.demo2", "com.mt.common","com.mt.sys","com.mt.dictionary","com.mt.my","com.mt.power","com.mt.healthcondition","com.mt.CenEAISiteRealtime"})
@MapperScan({"com.mt.auth","com.mt.demo","com.mt.demo2.mapper","com.mt.common", "com.mt.sys","com.mt.job.dao","com.mt.dictionary.mapper","com.mt.my.dao","com.mt.power.dao","com.mt.healthcondition.dao","com.mt.CenEAISiteRealtime.dao"})
@EnableTransactionManagement
public class RunApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
        System.out.println("   _____ __             __                                                          \n" +
                "  / ___// /_____ ______/ /_      __  ______     _______  _______________  __________\n" +
                "  \\__ \\/ __/ __ `/ ___/ __/_____/ / / / __ \\   / ___/ / / / ___/ ___/ _ \\/ ___/ ___/\n" +
                " ___/ / /_/ /_/ / /  / /_/_____/ /_/ / /_/ /  (__  ) /_/ / /__/ /__/  __(__  |__  ) \n" +
                "/____/\\__/\\__,_/_/   \\__/      \\__,_/ .___/  /____/\\__,_/\\___/\\___/\\___/____/____/  \n" +
                "                                   /_/                                              ");
    }

}
