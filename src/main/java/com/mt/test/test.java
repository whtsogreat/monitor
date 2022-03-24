//package com.mt.test;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.mt.power.dao.PowerDao;
//import com.mt.power.entity.PowerEntity;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Iterator;
//import java.util.List;
//
//@Component
//public class test {
//    @Autowired
//    private PowerDao powerDao;
//
//
//    @Test
//    public void contextLoads() throws Exception {
//        QueryWrapper<PowerEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("bomCode","ZGCBOMNUYLFI4N");
//        queryWrapper.between("time","1635350400000","1635436800000");
//        queryWrapper.eq("pointClass","PFs");
//
//        queryWrapper.orderByAsc("time");
//        List<PowerEntity> list = powerDao.selectList(queryWrapper);
//        Integer count = list.size();
//        System.out.println(count);
//    }
//
//
//    @Test
//    public void contextLoads2() throws Exception {
//        QueryWrapper<PowerEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("bomCode","ZGCBOMNUYLFI4N");
//        queryWrapper.between("time","1635350400000","1635436800000");
//        queryWrapper.eq("pointClass","PFs");
//        queryWrapper.orderByAsc("time");
//        List<PowerEntity> list = powerDao.selectList(queryWrapper);
//        Double oldValue = null;
//        Iterator<PowerEntity> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            PowerEntity power = iterator.next();
//            String value = power.getValue();
//            double temp = Double.parseDouble(value);
//            if (oldValue == null) {
//                oldValue = temp;
//            } else {
//                if (temp - oldValue > 1 || temp - oldValue < -1) {
//                    oldValue = temp;
//                } else {
//                    iterator.remove();
//                }
//            }
//        }
//        System.out.println(list.size());
//
//    }
//}
