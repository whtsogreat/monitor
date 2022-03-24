package com.mt.dictionary.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mt.dictionary.entity.Dictionary;

import java.util.List;

/**
 * 字典dao层接口
 */
@Component
public interface DictionaryMapper extends BaseMapper<Dictionary> {


    /**
     * 通过父级id查看全部信息
     * @param parentKey
     * @return
     */
    @Select("SELECT * FROM `t_dictionary` WHERE parent_key = #{parentKey} and use_unit=#{useUnit} ORDER BY field_sort*1 ASC")
     List<Dictionary> selectDictionaryByParentKey2(@Param("parentKey")String parentKey,String useUnit);
    @Select("SELECT * FROM `t_dictionary` WHERE parent_key = #{parentKey}  ORDER BY field_sort*1 ASC")
    List<Dictionary> selectDictionaryByParentKey(@Param("parentKey")String parentKey);

    @Select("SELECT * from t_dictionary  d  where d.field_key=#{fieldKey} ORDER BY field_sort*1 ASC")
    List<Dictionary> selectDictionaryByFieldKey(@Param("fieldKey")String fieldKey);
    //SELECT * from t_dictionary s  where s.field_value like '%_depot'  or s.field_value like '%_park'
    @Select("SELECT * from t_dictionary  where  field_value like '%_depot'  ORDER BY field_sort*1 ASC")
    List<Dictionary> selectAllDepot();
    @Select("SELECT * from t_dictionary  where  field_value like '%_park'  ORDER BY field_sort*1 ASC")
    List<Dictionary> selectAllPark();
    /**
     * 通过字段常量获取全部信息
     * @param constantKey
     * @return
     */
     @Select("SELECT t1.* FROM `t_dictionary` t,`t_dictionary` t1 WHERE t.constant_key =t1.parent_key AND t.constant_key=#{constantKey} ORDER BY t1.field_sort*1 ASC")
     List<Dictionary> selectDictionaryByConstant_key(@Param("constantKey") String constantKey);

   /**
    * 通过字段常量分页查询信息
    * @param page
    * @param constantKey
    * @return
    */
    @Select("SELECT t1.* FROM `t_dictionary` t,`t_dictionary` t1 WHERE t.constant_key =t1.parent_key AND t.constant_key=#{constantKey} ORDER BY t1.field_sort*1 ASC")
    IPage<Dictionary> selectDictionarPage(Page page,@Param("constantKey") String constantKey);



    /**
     * 通过字段值，父级信息查看信息
     * @param fieldValue
     * @param parentKey
     * @return
     */
    @Select("SELECT * FROM `t_dictionary` WHERE field_value = #{fieldValue} AND parent_key = #{parentKey} ORDER BY field_sort*1 ASC")
    List<Dictionary> selectByParentKeyAndFieldValue(@Param("fieldValue")String fieldValue,@Param("parentKey")String parentKey);

    /**
     * 通过CK，父级信息查看信息
     * @param constantKey
     * @param parentKey
     * @return
     */
    @Select("SELECT * FROM `t_dictionary` WHERE constant_key = #{constantKey} AND parent_key = #{parentKey}")
    Dictionary selectByParentKeyAndCK(@Param("constantKey")String constantKey,@Param("parentKey")String parentKey);

    /**
     * 通过id查看
     * @param id
     * @return
     */
    @Select("SELECT * FROM `t_dictionary` WHERE id = #{id}")
    Dictionary  selectByIdT(@Param("id")String id);


   /**
    *
    * @param dictionary
    * @return
    */
    @Update("UPDATE `t_dictionary` SET field_key = #{fieldKey} ,field_value=#{fieldValue},parent_key=#{parentKey},field_sort=#{fieldSort},field_note=#{fieldNote},update_user=#{updateUser},update_time=NOW() WHERE id=#{id} ")
    int  updateByIdT(Dictionary dictionary);

   /**
    * 通过id动态修改
    * @param dictionary
    * @return
    */
    @Update("<script>"
            +"UPDATE `t_dictionary`"
            +"<set>"
            +"<if test=\"fieldKey != null  and fieldKey != ''\">"
            +"field_key = #{fieldKey},"
            +"</if>"
            +"<if test=\"constantKey != null  and constantKey != ''\">"
            +"constant_key = #{constantKey},"
            +"</if>"
            +"<if test=\"fieldValue != null  and fieldValue != ''\">"
            +"field_value=#{fieldValue},"
            +"</if>"
            +"<if test=\"parentKey != null  and parentKey != ''\">"
            +"parent_key=#{parentKey},"
            +"</if>"
            +"<if test=\"useUnit != null  and useUnit != ''\">"
            +"use_unit=#{useUnit},"
            +"</if>"
            +"field_sort=#{fieldSort},"
            +"field_note=#{fieldNote},"
            +"<if test=\"isPageShow != null  and isPageShow != ''\">"
            +"is_page_show=#{isPageShow},"
            +"</if>"
            +"update_user=#{updateUser},"
            +"update_time=NOW()"
            +"</set>"
            +"WHERE id=#{id}"
            +"</script>"
    )
    int updateByIdDynamic(Dictionary dictionary);


    /**
     * 验证字段常量是否时唯一
     * @param constantKey
     * @return
     */
    @Select("SELECT * FROM `t_dictionary` WHERE constant_key = #{constantKey}")
    Dictionary checkConstantKeyExsits(@Param("constantKey")String constantKey) ;


   /**
    * 查询数量
    * @return
    */
   @Select("SELECT t1.field_key,COUNT(*) AS fieldCount FROM `t_dictionary` t ,`t_dictionary` t1 WHERE t.parent_key = t1.constant_key GROUP BY t.parent_key")
    List<Dictionary> getListCount();


    /**
     * 查询一级信息
     * @return
     */
    @Select("SELECT * FROM `t_dictionary` WHERE  parent_key IS  NULL ORDER BY field_sort*1 ASC")
    List<Dictionary> getListOne();

   /**
    * 查询一级信息
    * 分页
    * @param page
    * @return
    */
    @Select("SELECT * FROM `t_dictionary` WHERE  parent_key IS  NULL ORDER BY field_sort*1 ASC")
    IPage<Dictionary> getListOne(Page page);


   /**
    * 添加
    * @param dictionary
    * @return
    */
    @Insert("<script>"
            +"INSERT INTO `t_dictionary`"
            +"<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">"
            +"<if test=\"id != null and id != ''\">"
            +"id,"
            +"</if>"
            +"<if test=\"fieldKey != null and fieldKey != ''\">"
            +"field_key,"
            +"</if>"
            +"<if test=\"constantKey != null and constantKey != ''\" >"
            +"constant_key,"
            +"</if>"
            +"<if test=\"fieldValue != null and fieldValue != ''\">"
            +"field_value,"
            +"</if>"
            +"<if test=\"parentKey != null and parentKey != ''\">"
            +"parent_key,"
            +"</if>"
            +"<if test=\"useUnit != null and useUnit != ''\">"
            +"use_unit,"
            +"</if>"
            +"field_sort,"
            +"field_note,"
            +"<if test=\"createUser != null and createUser != ''\">"
            +"create_user,"
            +"</if>"
            +"<if test=\"createTime != null\">"
            +"create_time,"
            +"</if>"
            +"<if test=\"updateUser != null\">"
            +"update_user,"
            +"</if>"
            +"<if test=\"updateTime != null\">"
            +"update_time,"
            +"</if>"
            +"<if test=\"isPageShow != null and isPageShow != ''\">"
            +"is_page_show,"
            +"</if>"
            +"</trim>"
            +"<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">"
            +"<if test=\"id != null and id != ''\">"
            +"#{id},"
            +"</if>"
            +"<if test=\"fieldKey != null and fieldKey != ''\">"
            +"#{fieldKey},"
            +"</if>"
            +"<if test=\"constantKey != null and constantKey != ''\">"
            +"#{constantKey},"
            +"</if>"
            +"<if test=\"fieldValue != null and fieldValue != ''\">"
            +"#{fieldValue},"
            +"</if>"
            +"<if test=\"parentKey != null and parentKey != ''\">"
            +"#{parentKey},"
            +"</if>"
            +"<if test=\"useUnit != null and useUnit != ''\">"
            +"#{useUnit},"
            +"</if>"
            +"#{fieldSort},"
            +"#{fieldNote},"
            +"<if test=\"createUser != null and createUser != ''\">"
            +"#{createUser},"
            +"</if>"
            +"<if test=\"createTime != null\">"
            +"#{createTime},"
            +"</if>"
            +"<if test=\"updateUser != null\">"
            +"#{updateUser},"
            +"</if>"
            +"<if test=\"updateTime != null\">"
            +"#{updateTime},"
            +"</if>"
            +"<if test=\"isPageShow != null\">"
            +"#{isPageShow},"
            +"</if>"
            +"</trim>"
            +"</script>"
    )
    int insertDictionary(Dictionary dictionary);

}
