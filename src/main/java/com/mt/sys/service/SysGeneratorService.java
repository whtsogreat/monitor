/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.mt.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.mt.sys.dao.ColumnEntityDao;
import com.mt.sys.dao.GeneratorDao;
import com.mt.sys.dao.MySQLGeneratorDao;
import com.mt.sys.entity.ColumnEntity;
import com.mt.utils.GenUtils;
import com.mt.utils.PageUtils;
import com.mt.utils.Query;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysGeneratorService {
    @Autowired
    private GeneratorDao generatorDao;
    @Autowired
    private  ColumnEntityDao columnEntityDao;


    public PageUtils queryList(Query query) {
        com.github.pagehelper.Page<Object> page = PageHelper.startPage(query.getPage(), query.getLimit());
        List<Map<String, Object>> list = generatorDao.queryList(query);
        int total = (int) page.getTotal();
//        if (generatorDao instanceof MongoDBGeneratorDao) {
//            total = MongoDBCollectionFactory.getCollectionTotal(query);
//        }
        return new PageUtils(list, total, query.getLimit(), query.getPage());
    }

    public Map<String, String> queryTable(String tableName) {
        return generatorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorDao.queryColumns(tableName);
    }


    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
//        if (MongoManager.isMongo()) {
//            GenUtils.generatorMongoCode(tableNames, zip);
//        }


        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    public byte[] generatorCode2(String tableName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
            //查询表信息
            Map<String, String> table = queryTable(tableName);
        QueryWrapper<ColumnEntity> columnEntityQueryWrapper = new QueryWrapper<>();
        columnEntityQueryWrapper.eq("table_name",tableName);
        columnEntityQueryWrapper.orderByAsc("sort");
        List<ColumnEntity> columnEntities = columnEntityDao.selectList(columnEntityQueryWrapper);

        //生成代码
            GenUtils.generatorCode2(table,columnEntities, zip);

//        if (MongoManager.isMongo()) {
//            GenUtils.generatorMongoCode(tableNames, zip);
//        }


        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    public void insert(ColumnEntity columnEntity) {
        columnEntityDao.insert(columnEntity);
    }

    public List<ColumnEntity> selectByTableName(String tableName) {
        QueryWrapper<ColumnEntity> columnEntityQueryWrapper = new QueryWrapper<>();
        columnEntityQueryWrapper.eq("table_name",tableName);
        columnEntityQueryWrapper.orderByAsc("sort");
        List<ColumnEntity> columnEntities = columnEntityDao.selectList(columnEntityQueryWrapper);
        return columnEntities;
    }

    public void deleteByTableName(String tableName) {
        QueryWrapper<ColumnEntity> columnEntityQueryWrapper = new QueryWrapper<>();
        columnEntityQueryWrapper.eq("table_name",tableName);
        int delete = columnEntityDao.delete(columnEntityQueryWrapper);
    }
}
