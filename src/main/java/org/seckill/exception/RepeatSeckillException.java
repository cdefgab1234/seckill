package org.seckill.exception;

/**重复秒杀异常（运行时异常）
 * Created by Administrator on 2017/9/26.
 */
public class RepeatSeckillException extends SeckillException {

    public RepeatSeckillException(String message) {
        super(message);
    }

    public RepeatSeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
