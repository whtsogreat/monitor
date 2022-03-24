package com.mt.demo2.service.impl;

import com.mt.demo2.entity.RiskVO;
import com.mt.demo2.mapper.RiskMapper;
import com.mt.demo2.service.RiskService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.util.*;

/**
 * @Author: wss
 * @Date: 15:14 2021/7/1
 * @Description: 解析excel
 */
@Service("riskService")
public class RiskServiceImpl implements RiskService {
    @Autowired
    private RiskMapper riskMapper;

    @Override
    public boolean excelParse(String fileName, MultipartFile file) throws Exception {
        boolean isOK = false;
        List<RiskVO> riskVOList = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new Exception("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            isOK = true;
        }
        RiskVO riskVO;
        for (int r = 3; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            String id = row.getCell(0).getStringCellValue();
            String name = row.getCell(1).getStringCellValue();
            String gender = row.getCell(2).getStringCellValue();
            String age = row.getCell(3).getStringCellValue();
            String address = row.getCell(4).getStringCellValue();
            String idnumber = row.getCell(5).getStringCellValue();
            String reason = row.getCell(6).getStringCellValue();
            String introduction = row.getCell(7).getStringCellValue();
            String process = row.getCell(8).getStringCellValue();
            String createtime = row.getCell(9).getStringCellValue();

            // 判断非空，若空不执行本次数据录入
            if (id == null || id.isEmpty()) {
                continue;
            }
            if (name == null || name.isEmpty()) {
                continue;
            }
            if (gender == null || gender.isEmpty()) {
                continue;
            }
            if (age == null || age.isEmpty()) {
                continue;
            }
            if (address == null || address.isEmpty()) {
                continue;
            }
            if (idnumber == null || idnumber.isEmpty()) {
                continue;
            }
            if (reason == null || reason.isEmpty()) {
                continue;
            }
            if (introduction == null || introduction.isEmpty()) {
                continue;
            }
            if (process == null || process.isEmpty()) {
                continue;
            }
            if (createtime == null || createtime.isEmpty()) {
                continue;
            }

            riskVO = new RiskVO();
            /*String gender1;
            Map<String, String> map1 = riskMapper.selectGenderCode(gender);
            if (map1 != null) {
                gender1 = map1.get("code");
                riskVO.setGender(gender1);
            } else {
                continue;
            }*/

            riskVO.setId(UUID.randomUUID().toString());
            riskVO.setName(name);
            riskVO.setAge(age);
            riskVO.setAddress(address);
            riskVO.setIdnumber(idnumber);
            riskVO.setReason(reason);
            riskVO.setGender(gender);
            riskVO.setIntroduction(introduction);
            riskVO.setProcess(process);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd, HH-mm-ss");
            riskVO.setCreatetime(simpleDateFormat.parse(createtime));
            riskVOList.add(riskVO);
        }
        for (RiskVO temp : riskVOList) {
            int num = riskMapper.insertRiskVO(temp);
            if (num > 0) {
                isOK = true;
            }
        }
        return isOK;
    }
}
