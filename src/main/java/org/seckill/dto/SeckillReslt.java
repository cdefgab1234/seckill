package org.seckill.dto;

/**
 * Created by Administrator on 2017/10/1.
 */
//所有ajax请求的返回类型，封装 JSON 结果
public class SeckillReslt<T> {

    private boolean success;

    private T data;

    private String error;

    public SeckillReslt(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillReslt(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
