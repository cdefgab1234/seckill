//存放交互逻辑js代码
//javascript模块化
var seckill = {
    //封装秒杀ajax的url
    URL:{

    },
    validatePhone:function(phone){
        if (phone && phone.length==11 && !isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },
    //详情页秒杀逻辑
    detail:{
        //详情页初始化：
        init:function(params){
            //手机验证和登陆，计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //验证手机号
            if (!seckill.validatePhone(killPhone)){
                //如果没有登陆绑定phone
                //控制输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show:true,//显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });
            }

            $('#killPhoneBtn').click(function () {
                var inputPhone = $('#killPhoneKey').val();
                console.log("inputPhone: " + inputPhone);
                if (seckill.validatePhone(inputPhone)) {
                    //电话写入cookie(7天过期)
                    $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                    //验证通过　　刷新页面
                    window.location.reload();
                } else {
                    //todo 错误文案信息抽取到前端字典里
                    $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                }
            });
        }
    }

}