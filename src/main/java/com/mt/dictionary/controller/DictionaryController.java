package com.mt.dictionary.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mt.auth.hr.HrService;
import com.mt.auth.hr.HrUserEmp;
import com.mt.auth.utils.JsonResponse;
import com.mt.auth.utils.TokenUtils;
import com.mt.dictionary.entity.Dictionary;
import com.mt.dictionary.mapper.DictionaryMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;



/**
 * 字典管理
 *
 * @author liujie
 * @date 2019/12/11 13:21
 * @Version 1.0
 */
@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final @NonNull DictionaryMapper dictionaryMapper;//字典接口类

    private final @NonNull TokenUtils tokenUtils;//引入登录用户权限类

    private  final  @NonNull HrService hrService;

    /**
     * 计数
     * @return
     */
    @GetMapping("/dictionary/getListCount")
    public JsonResponse getListCount() {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }

            jsonResponse.put("msg",dictionaryMapper.getListCount());
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }
    //获取车辆段与停车场数据
    @GetMapping("/dictionary/getAllDepotPark")
    public JsonResponse getAllDepotPark() {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            List<Dictionary> depots = dictionaryMapper.selectAllDepot();
            List<Dictionary> parks = dictionaryMapper.selectAllPark();
            jsonResponse.put("depots",depots);
            jsonResponse.put("parks",parks);

            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }

    /**
     * (已测试)
     * 验证字段常量是否是唯一
     * @param constantKey
     * @return
     */
    @RequestMapping("/dictionary/checkByConstantKey")
    @Transactional
    public JsonResponse checkByConstantKey(String constantKey) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            Dictionary dictionary = dictionaryMapper.checkConstantKeyExsits(constantKey);
            if(dictionary != null){
                return  jsonResponse.setFailed(1,"参数不唯一");
            }

            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }

    }


    /**
     * (已测试)
     * 通过主键id查询信息
     * @return
     */
    @RequestMapping("/dictionary/dictionaryById/{id}")
    @Transactional
    public JsonResponse dictionaryById(@PathVariable String id) {

        JsonResponse jsonResponse = new JsonResponse();

        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            if (id == null) {
                return jsonResponse.setFailed(1, "参数不正确");
            }

            jsonResponse.put("dictionary",dictionaryMapper.selectByIdT(id));
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }

    }


    /**
     * (已测试)
     * 通过字段值，父级常量，查询信息
     * @return
     */
    @RequestMapping("/dictionary/listByParentKeyAndFieldValue")
    @Transactional
    public JsonResponse dictionaryListByParentKeyAndFieldValue(String fieldValue, String parentKey) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }

            jsonResponse.put("dictionaryByParentKeyAndFieldValue",dictionaryMapper.selectByParentKeyAndFieldValue(fieldValue,parentKey));

            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }

    }

    /**
     * (已测试)
     * 通过字段值，父级常量，查询信息
     * @return
     */
    @RequestMapping("/dictionary/listByParentKeyAndCK")
    @Transactional
    public JsonResponse dictionaryListByParentKeyCK(String constantKey, String parentKey) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }

            jsonResponse.put("dictionaryByParentKeyAndCK",dictionaryMapper.selectByParentKeyAndCK(constantKey,parentKey));

            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }

    }


    /**
     * (已测试)
     * 通过字段常量查询全部信息
     * @param constantKey
     * @return
     */
    @RequestMapping("/dictionary/listByConstant")
    @Transactional
    public JsonResponse dictionaryListByConstantKey(String constantKey) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
              if(constantKey == null || constantKey == "") {
                  jsonResponse.put("dictionaryByConstant",dictionaryMapper.getListOne());
              }else{

                  jsonResponse.put("dictionaryByConstant",dictionaryMapper.selectDictionaryByConstant_key(constantKey));
              }
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }


    }


    /**
     * (已测试)
     * 通过父级id查看全部信息
     * @param parentKey
     * @return
     */
    @RequestMapping("/dictionary/listByPar")
    @Transactional
    public JsonResponse dictionaryListByPar(String parentKey) {
        JsonResponse jsonResponse = new JsonResponse();
        try {

            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            boolean b = hrService.userHasPerm(user, "sys:check:dc");
            if(parentKey.equals("SUBWAY_LINE")&&b==false){
               if(user.getCompNo().equals("1010") || user.getCompNo().equals("1020") || user.getCompNo().equals("1030") || user.getCompNo().equals("1040")){
                   jsonResponse.put("dictionary",dictionaryMapper.selectDictionaryByParentKey2(parentKey,user.getCompNo()));
               }else{
                   jsonResponse.put("dictionary",dictionaryMapper.selectDictionaryByParentKey(parentKey));

               }
           }else{
               jsonResponse.put("dictionary",dictionaryMapper.selectDictionaryByParentKey(parentKey));

           }

            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }

    @RequestMapping("/dictionary/listByFieldKey")
    @Transactional
    public JsonResponse dictionaryListByFieldKey(String fieldKey) {
        JsonResponse jsonResponse = new JsonResponse();
        try {

            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            jsonResponse.put("dictionary",dictionaryMapper.selectDictionaryByFieldKey(fieldKey));


            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }
    /**
     * (已测试)
     * 分页
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/dictionary/list")
    public JsonResponse getAllDictionary(String constantKey,Integer pageNo, Integer pageSize) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            if(pageNo == null)  pageNo=1;
            if(pageSize== null) pageSize=10;
            Page<Dictionary> page = new Page<>(pageNo, pageSize);
            if(constantKey == null || constantKey == "") {
                jsonResponse.put("page",dictionaryMapper.getListOne(page));
            }else {

                jsonResponse.put("page",dictionaryMapper.selectDictionarPage(page,constantKey));
            }
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }

    @GetMapping("/dictionary/list2")
    public JsonResponse getAllDictionary2(Integer pageNo, Integer pageSize) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            if(pageNo == null)  pageNo=1;
            if(pageSize== null) pageSize=10;
            Page<Dictionary> page = new Page<>(pageNo, pageSize);
            QueryWrapper<Dictionary> wrapper = new QueryWrapper<>();
            jsonResponse.put("page",dictionaryMapper.selectPage(page,wrapper));
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }



    /**
     * (已测试)
     * 添加
     * @param dictionary
     * @return
     */
    @RequestMapping("/dictionary/add")
    @Transactional
    public JsonResponse dictionaryAdd(Dictionary dictionary) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"没有权限");
            }

            dictionary.setCreateUser(user.getLoginName());
            dictionary.setId(UUID.randomUUID().toString().replace("-", ""));
            dictionary.setCreateTime(new Date());
            if(dictionaryMapper.insertDictionary(dictionary) != 1){
                return jsonResponse.setFailed(2,"添加失败");
            }

            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }





    /**
     * (已测试)
     * 通过主键id删除
     * @param id
     * @return
     */
    @RequestMapping("/dictionary/delete/{id}")
    @Transactional
    public JsonResponse dictionaryDelete(@PathVariable String id) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            if (id == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            Dictionary dictionary = dictionaryMapper.selectByIdT(id);
            String constantKey = dictionary.getConstantKey();
            String parentKey1 = dictionary.getParentKey();
            if(constantKey !=null || constantKey != "" && parentKey1==null || parentKey1 ==""  ){
                    //如果是一级
                  //通过一级的常量查询二级信息
                List<Dictionary> dictionaries1 = dictionaryMapper.selectDictionaryByParentKey(constantKey);
                for (int i = 0; i <dictionaries1.size() ; i++) {
                    String constantKey1 = dictionaries1.get(i).getConstantKey();//获取二级常量

                    List<Dictionary> dict = dictionaryMapper.selectDictionaryByParentKey(constantKey1);//通过二级常量获取三级信息
                    for (int j = 0; j < dict.size(); j++) {
                        String idS = dict.get(j).getId();//三级id
                        //删除三级
                        if (dictionaryMapper.deleteById(idS) != 1) {
                            return jsonResponse.setFailed(2,"失败");
                        }
                    }
                    String idE = dictionaries1.get(i).getId();
                    //删除二级
                    if (dictionaryMapper.deleteById(idE) != 1) {
                        return jsonResponse.setFailed(2,"失败");
                    }
                }
                //删除一级
                if (dictionaryMapper.deleteById(id) != 1) {
                    return jsonResponse.setFailed(2,"失败");
                }
            }else if(constantKey !=null || constantKey != "" && parentKey1 !=null || parentKey1 !="" ) {
                //如果是二级
                //通过二级的常量查询三级信息
                List<Dictionary> dictionaries1 = dictionaryMapper.selectDictionaryByParentKey(constantKey);
                for (int i = 0; i <dictionaries1.size() ; i++) {
                    String idS = dictionaries1.get(i).getId();
                    //如果是三级，直接删除
                    if (dictionaryMapper.deleteById(idS) != 1) {
                        return jsonResponse.setFailed(2,"失败");
                    }
                }
                //删除二级
                if (dictionaryMapper.deleteById(id) != 1) {
                    return jsonResponse.setFailed(2,"失败");
                }
            }else if(constantKey == null || constantKey == ""){
                //如果是三级，直接删除
                if (dictionaryMapper.deleteById(id) != 1) {
                    return jsonResponse.setFailed(2,"失败");
                }
            }
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }
    }


    /**
     * （已测试）
     * 修改(禁止修改字段常量)
     * @param dictionary
     * @return
     */
    @RequestMapping("/dictionary/edit")
    @Transactional
    public JsonResponse dictionaryEdit(Dictionary dictionary) {
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
        try {
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            if (dictionary == null) {
                return jsonResponse.setFailed(2,"失败");
            }

            if (dictionaryMapper.updateByIdT(dictionary) != 1) {
                return jsonResponse.setFailed(2,"失败");
            }else{


                dictionary.setUpdateUser(user.getLoginName());
                dictionary.setCreateTime(new Date());
                return jsonResponse.setSuccess("OK");
            }
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }

    }


    /**
     * （已测试）
     * 修改(禁止修改字段常量)
     * @param dictionary
     * @return
     */
    @RequestMapping("/dictionary/editDynamic")
    @Transactional
    public JsonResponse dictionaryEditDynamic(Dictionary dictionary) {
        JsonResponse jsonResponse = new JsonResponse();
        //检查登录用户权限
        try {
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"没有登录");
            }
            if (dictionary == null) {
                return jsonResponse.setFailed(2,"没有查询到字典");
            }

            if (dictionaryMapper.updateByIdDynamic(dictionary) != 1) {
                return jsonResponse.setFailed(2,"修改失败");
            }else{

                dictionary.setUpdateUser(user.getLoginName());
                dictionary.setCreateTime(new Date());
                return jsonResponse.setSuccess("OK");
            }
        } catch (Exception e) {
           return jsonResponse.setFailed(2,e.getLocalizedMessage());
        }

    }




    /**
     *  查询二级和三级
     * @return
     */
    @GetMapping("/dictionary/seconList")
    public JsonResponse find() {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<Dictionary>();
//            queryWrapper.eq("is_page_show","true");
            List<Dictionary> lists = dictionaryMapper.selectList(queryWrapper.orderByAsc("field_sort"));

            Map<String,JSONObject> resultM = new HashMap<String,JSONObject>();

            // 查找所有,筛选出一级、二级字典
            for (int i = 0; i < lists.size() ; i++) {
                Dictionary dict = lists.get(i);
                // 一级
                if(dict.getParentKey()==null){
                    JSONObject json = new JSONObject();
                    json.put("constant_key",dict.getConstantKey());
                    json.put("field_key",dict.getFieldKey());
                    json.put("level",1);
                    json.put("isPageShow",dict.getIsPageShow());
                    json.put("bean",dict);
                    json.put("subs",new ArrayList<Dictionary>());
                    resultM.put(dict.getConstantKey(),json);
                }
                // 二级
                if(dict.getParentKey()!=null && dict.getFieldValue() == null){
                    // 创建二级字典的JSON对象以及它的subs数组
                    JSONObject json = new JSONObject();
                    json.put("constant_key",dict.getConstantKey());
                    json.put("field_key",dict.getFieldKey());
                    json.put("level",2);
                    json.put("isPageShow",dict.getIsPageShow());
                    json.put("subs",new ArrayList<Dictionary>());
                    resultM.put(dict.getConstantKey(),json);
                }

            }


            // 遍历所有三级菜单
            for (int k = 0; k < lists.size() ; k++) {
                Dictionary dict = lists.get(k);

                if(dict.getParentKey()!=null && dict.getFieldValue() == null){

                    // 将二级的字典添加到一级字典的subs中
                    String parentKey = dict.getParentKey();
                    JSONObject json = resultM.get(parentKey);
                    if(json!=null){
                        List<Dictionary> subs = (List<Dictionary>) json.get("subs");
                        if(subs!=null){
                            subs.add(dict);
                        }
                    }

                }
                // 将三级的字典添加到二级字典的subs中
                if(dict.getConstantKey() == null && dict.getIsPageShow().equals("true")){

                    String parentKey = dict.getParentKey();
                    JSONObject json = resultM.get(parentKey);
                    if(json != null){
                        List<Dictionary> subs = (List<Dictionary>) json.get("subs");
                        if(subs!=null){
                            subs.add(dict);
                        }
                    }

                }
            }

//            for (Map.Entry<String, JSONObject> entry : resultM.entrySet()) {
//                System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
//                System.out.println("==========");
//            }
            //System.out.println(firstM.toString());
            //jsonResponse.put("firstM",firstM);
            jsonResponse.put("dataList",resultM);
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            e.printStackTrace();
           return jsonResponse.setFailed(2,e.getMessage());
        }
    }

    /**
     *  查询二级
     * @return
     */
    @GetMapping("/dictionary/seconList2")
    public JsonResponse find2() {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //检查登录用户权限
            HrUserEmp user = tokenUtils.getUserInfo();
            if (user == null) {
                return jsonResponse.setFailed(2,"失败");
            }
            QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<Dictionary>();
            queryWrapper.isNotNull("constant_key");
            queryWrapper.isNotNull("parent_key");
            List<Dictionary> lists = dictionaryMapper.selectList(queryWrapper.orderByAsc("field_sort"));

            jsonResponse.put("dataList",lists);
            return jsonResponse.setSuccess("OK");
        } catch (Exception e) {
            e.printStackTrace();
           return jsonResponse.setFailed(2,e.getMessage());
        }
    }




    /**
     *  站点多选
     *  查询二级和三级
     * @return
     */
    @GetMapping("/dictionary/multipleList/checkPlace")
    public JsonResponse findMultiple(String checkPlace) {
        JsonResponse jsonResponse = new JsonResponse();
        if(checkPlace==null || checkPlace.equals("")){
            return jsonResponse.setFailed(2,"参数出错，请检查参数。");
        }
        String[] split = checkPlace.split(";");

        String subs="";
        String fieldKey="";
        String  s1="";
            for(int i=0;i<split.length;i++){
                String sub_way = split[i].substring(0, split[i].indexOf("-"));//取值线路
                QueryWrapper<Dictionary> queryWarpper = new QueryWrapper<>();
                queryWarpper.eq("constant_key",sub_way);
                Dictionary dictionary = dictionaryMapper.selectOne(queryWarpper);
                String sub_wayZ = dictionary.getFieldKey();//线路中文名称
                System.out.println("获取线路==================================="+sub_wayZ);
                String  sub_place = split[i].substring(split[i].indexOf("-")+1, split[i].lastIndexOf("-"));//取值地点
                String sub_placeZ="";
                String sub_station="";
                //判断取值地点不为空
                if(sub_place!= null && !sub_place.equals("")){
                     sub_placeZ = sub_place.equals("A")?"车站":sub_place.equals("B")?"车辆段":sub_place.equals("C")?"停车场":sub_place.equals("D")?"无":"暂无数据";
                    // subs=subs+"；"+sub_placeZ+"--";

                    //判断地点不为空
                    if(sub_placeZ != null && !sub_placeZ.equals("") ){
                        sub_station = split[i].substring(split[i].lastIndexOf("-") + 1, split[i].length());//取值最后的站点

                        String sub_stationZ="";
                        //判断最后一位值不为空
                        if(sub_station !=null && !sub_station.equals("")){
                                if(sub_station.contains(",")){
                                    String[] split1 = sub_station.split(",");

                                    for(int m=0;m<split1.length;m++){
                                        String s = split1[m];//站点
                                        QueryWrapper<Dictionary> queryWarpper2 = new QueryWrapper<>();
                                        queryWarpper2.eq("field_value",s);
                                        Dictionary dictionary2 = dictionaryMapper.selectOne(queryWarpper2);
                                        String sub_stationC = dictionary2.getFieldKey();//站点中文名称

                                        //sub_stationZ=sub_stationC+"，"+sub_stationZ;
                                        sub_stationZ=sub_stationZ+"，"+sub_stationC;
                                    }
                                    subs=subs+"；"+sub_wayZ+"--"+sub_placeZ+"--"+sub_stationZ.substring(sub_stationZ.indexOf("，")+1,sub_stationZ.length());
                                }else if(!sub_station.contains(",") && sub_station !=null && !sub_station.equals("")){

                                    QueryWrapper<Dictionary> queryWarpper2 = new QueryWrapper<>();
                                    queryWarpper2.eq("field_value",sub_station);
                                    Dictionary dictionary2 = dictionaryMapper.selectOne(queryWarpper2);
                                    String sub_stationC = dictionary2.getFieldKey();//站点中文名称
                                    sub_stationZ=sub_stationC;

                                    subs=subs+"；"+sub_wayZ+"--"+sub_placeZ+"--"+sub_stationZ;
                                }

                        }else{
                            //最后一位值如果为空
                            subs=subs+"；"+sub_wayZ+"--"+sub_placeZ+"--";
                        }

                    }

                }else{
                    //判断中间值为空
                    subs=subs+"；"+sub_wayZ+"--";
                }


            }
        jsonResponse.put("dataList",subs.substring(subs.indexOf("；")+1,subs.length()));
        return jsonResponse.setSuccess("OK");

    }
}
