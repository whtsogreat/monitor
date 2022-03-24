package com.mt.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mt.sys.entity.TableEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据库接口
 *
 * @author
 * @since 2018-07-24
 */
@Component
public interface GeneratorDao extends BaseMapper<TableEntity> {

    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables where table_schema = (select database()) order by create_time desc")
    List<Map<String, Object>> queryList(Map<String, Object> map);
    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables where table_schema = (select database()) and table_name = #{tableName}")
    Map<String, String> queryTable(String tableName);
    @Select("select column_name columnName, data_type dataType,column_type columnType,numeric_scale numericScale, column_comment comments, column_key columnKey, extra from information_schema.columns\n" +
            "  where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<Map<String, String>> queryColumns(String tableName);
}
