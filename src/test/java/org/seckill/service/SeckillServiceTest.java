package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatSeckillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/9/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        {"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {

        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void testGetById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    //将下面俩个方法整合到一个逻辑里测试
    //测试代码完整逻辑，可重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        long id =1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long phone = 1350129283;
            String md5 =exposer.getMd5();
            try{
                SeckillExecution execution=seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}",execution);
            }catch (RepeatSeckillException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void testExportSeckillUrl() throws Exception {
        long id =1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
    }

    @Test
    public void testExecuteSeckill() throws Exception {
        long id = 1001;
        long phone = 1350129183;
        String md5 ="38225aad86bf016480689b39a7be9f3f";
        try{
            SeckillExecution execution=seckillService.executeSeckill(id,phone,md5);
            logger.info("result={}",execution);
        }catch (RepeatSeckillException e1){
            logger.error(e1.getMessage());
        }catch (SeckillCloseException e2){
            logger.error(e2.getMessage());
        }
    }
}