package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatSeckillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired//他妈的报了一天的错，忘了加分号
    private SeckillService seckillService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(Model model){
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        //list.jsp+model =modelanfview
        return "list";///WEB-INF/"list".jsp
    }

    /**
     * forward是服务器请求资源,服务器直接访问目标地址的URL,把那个URL的响应内容读取过来,然后把这些内容再发给浏览器.浏览器根本不知道服务器发送的内容从哪里来的,所以它的地址栏还是原来的地址.
       redirect是服务端根据逻辑,发送一个状态码,告诉浏览器重新去请求那个地址.所以地址栏显示的是新的URL.
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
           return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    //ajax ,json暴露秒杀接口的方法
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId)
    {
        SeckillResult<Exposer> result;
        try{
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e) {
            e.printStackTrace();
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }

        return result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/execution",
                  method = RequestMethod.POST,
                  produces = {"application/json;charset=UTF-8"})//这里注意是分号，看报错一定要看cause by，切记
    @ResponseBody
    public SeckillResult<SeckillExecution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "userPhone",required = false) Long phone)
    {
        System.out.println(phone);
        if(phone==null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        //上面@CookieValue的require为false表示没有该参数也可以，这个值是从cookie中获取的登陆参数，感觉比@requireParam更靠谱一点
        SeckillResult<SeckillExecution> result;
        try {
            //存储过程调用
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch (RepeatSeckillException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch (SeckillCloseException e){
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true,execution);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true,execution);
        }
    }

    //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }


}
