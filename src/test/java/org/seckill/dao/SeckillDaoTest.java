package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by codingBoy on 16/11/27.
 * 配置spring和junit整合，这样junit在启动时就会加载spring容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;


    @Test
    public void testQueryById() throws Exception {
        int n = 1000;
        long seckillId=1000;
        Seckill seckill=seckillDao.queryById(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);


    }

    @Test
    public void testQueryAll() throws Exception {

        List<Seckill> seckills = seckillDao.queryAll(0,5);
        for(Seckill seckill:seckills){
            System.out.println(seckill);
        }
    }

    @Test
    public void testReduceNumber() throws Exception {
        /*
        面向接口编程时，数据必须绑定，否则会报错误提示
         */
        Date killtime = new Date();
        int updataCount = seckillDao.reduceNumber(1000,killtime);
        System.out.println("updataCount被影响的行数："+updataCount);


    }

}