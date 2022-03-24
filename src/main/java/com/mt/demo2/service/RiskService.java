package com.mt.demo2.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: wss
 * @Date: 15:10 2021/7/1
 * @Description: 解析excel
 */
public interface RiskService {
    boolean excelParse(String fileName, MultipartFile file) throws Exception;
}
