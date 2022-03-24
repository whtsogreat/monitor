package com.mt.common.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 */
@Component
@ConfigurationProperties(prefix = "censoft")
public class ProjectConfig
{

    /** 上传路径 */
    private static String profile;


    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        ProjectConfig.profile = profile;
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/upload";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
