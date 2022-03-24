package com.mt.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang3.StringUtils;

/**
 * 列的属性
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月20日 上午12:01:45
 */
public class ColumnEntity {
	private static final long serialVersionUID = 1L;

	/** 编号 */
	private Long columnId;

	/** 归属表编号 */
	private Long tableId;
	private String tableName;
	//列名
    private String columnName;
    //列名类型
    private String dataType;
    //列名备注
    private String comments;
    
    //属性名称(第一个字母大写)，如：user_name => UserName
	@TableField(exist = false)
    private String attrName;
    //属性名称(第一个字母小写)，如：user_name => userName
    private String attrName2;
    //属性类型
    private String attrType;
    //auto_increment
    private String extra;

    /** 是否必填（1是） */
    private String isRequired;

    /** 是否为插入字段（1是） */
    private String isInsert;

    /** 是否编辑字段（1是） */
    private String isEdit;

    /** 是否列表字段（1是） */
    private String isList;

    /** 是否查询字段（1是） */
    private String isQuery;

    /** 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围） */
    private String queryType;

    /** 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件） */
    private String htmlType;

    /** 字典类型 */
    private String dictType;

    /** 排序 */
    private Integer sort;

    
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getAttrName2() {
		return attrName2;
	}
	public void setAttrName2(String attrName2) {
		this.attrName2 = attrName2;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getAttrType() {
		return attrType;
	}
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getIsInsert() {
		return isInsert;
	}

	public void setIsInsert(String isInsert) {
		this.isInsert = isInsert;
	}
    public boolean isInsert()
    {
        return isInsert(this.isInsert);
    }

    public boolean isInsert(String isInsert)
    {
        return isInsert != null && StringUtils.equals("1", isInsert);
    }

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}
    public boolean isEdit()
    {
        return isInsert(this.isEdit);
    }

    public boolean isEdit(String isEdit)
    {
        return isEdit != null && StringUtils.equals("1", isEdit);
    }

	public String getIsList() {
		return isList;
	}

	public void setIsList(String isList) {
		this.isList = isList;
	}
    public boolean isList()
    {
        return isList(this.isList);
    }

    public boolean isList(String isList)
    {
        return isList != null && StringUtils.equals("1", isList);
    }

	public String getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}
    public boolean isQuery()
    {
        return isQuery(this.isQuery);
    }

    public boolean isQuery(String isQuery)
    {
        return isQuery != null && StringUtils.equals("1", isQuery);
    }

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getHtmlType() {
		return htmlType;
	}

	public void setHtmlType(String htmlType) {
		this.htmlType = htmlType;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getSort()
    {
        return sort;
    }



	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
