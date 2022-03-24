//package com.mt.test;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.mt.power.dao.LoadRateDao;
//import com.mt.power.entity.LoadRateEntity;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.InputStream;
//import java.util.List;
//
//public class javaTest {
//    private InputStream in;
//    private SqlSessionFactory factory;
//    private SqlSession session;
//    private  LoadRateDao loadRateDao;
//
//    @Before//junit 的注解
//    public void init()throws Exception{
//        //1.读取配置文件
//        in = Resources.getResourceAsStream("");
//        //2.创建工厂
//        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//        factory = builder.build(in);
//        //3.创建 session
//        session = factory.openSession();
//        //4.创建代理对象
//        loadRateDao = session.getMapper(LoadRateDao.class);
//    }
//
//    @After   //junit 的注解
//    public void destroy()throws Exception {
//        //提交事务
//        session.commit();
//        //释放资源
//        session.close();
//        //关闭流
//        in.close();
//    }
//
//    @Test
//    public void test(){
//        System.out.println("使用测试接口类");
//        QueryWrapper<LoadRateEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("equip_code", "ZGCCBI24FXZOY");
//
//        List<LoadRateEntity> list = loadRateDao.selectList(queryWrapper);
//        Integer count = list.size();
//        System.out.println(count);
//    }
//}
