package com.xiao.lib.http.bean;

/**
 * @author shen
 * @date 2017/5/5
 * 后台返回的基本格式
 */
public class BaseResponse<T> {
    private int result;
    private T msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}
