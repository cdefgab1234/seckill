package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/9/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void testInsertSuccessKilled() throws Exception {
        //第一次: 1
        //第二次: 0
        //ignore 增加重复不报错，但是你加不进去,很不错
        long seckillId = 1001;
        long phone = 13576235421L;
        int insertCount = successKilledDao.insertSuccessKilled(seckillId,phone);
        System.out.println("insertCount:"+insertCount);
    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {
        long seckillId = 1001;
        long phone = 13576235421L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}