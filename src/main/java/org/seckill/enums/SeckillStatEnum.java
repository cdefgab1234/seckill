package org.seckill.enums;

/**
 * 使用枚举表述我们的常亮数据字段
 * Created by Administrator on 2017/9/26.
 */
public enum  SeckillStatEnum {

    SUCCESS(1,"秒杀成功！"),
    END(0,"秒杀结束！"),
    REPEAT_KILL(-1,"重复秒杀！"),
    INNER_ERROR(-2,"系统异常！"),
    DATA_REWRITE(-3,"数据篡改！");

    private int state;

    private String stateInfo;

    private SeckillStatEnum( int state,String stateInfo) {
        this.stateInfo = stateInfo;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        //enum的values()方法，好高端学习了
        for(SeckillStatEnum state:values()){
            if(state.getState() == index){
                return state;
            }
        }
        return null;
    }
}
