package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatSeckillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2017/9/26.
 */
public class SeckillServiceImpl implements SeckillService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    //md5盐值字符串用于混淆MD5
    private final String slat = "dfdajdjnfjnisnf#fsf$";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return new  Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前系统的时间
        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化字符串，不可逆
        String md5 = getMd5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMd5(long seckillId){
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatSeckillException, SeckillCloseException {

        if(md5 == null || md5.equals(getMd5(seckillId))){
            throw new SeckillCloseException("seckill data rewrite");
        }
        //执行秒杀逻辑减库存+记录秒杀逻辑
        Date nowTime = new Date();
        //减库存
        try {

            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if(updateCount <=0){
                //没有更新到记录，秒杀结束
                throw new SeckillCloseException("seckill is closed!");
            }else {
                //记录秒杀逻辑
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount<=0){
                    //重复秒杀
                    throw new RepeatSeckillException("seckill repeat!");
                }else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatSeckillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译期异常转换成运行期异常
            throw new SeckillException("seckill inner error"+e.getMessage());
        }

    }
}
