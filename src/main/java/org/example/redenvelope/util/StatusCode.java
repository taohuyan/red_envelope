package org.example.redenvelope.util;

import lombok.Data;

/**
 * 创建人：Jason
 * 创建时间：2023/6/7
 * 描述你的类：
 */

public enum StatusCode {
    Success(0,"成功"),Fail(-1,"失败"),InvalidPrams(201,"非法的参数");
    private Integer code;//状态码
    private String msg;//描述信息

    StatusCode(Integer code,String msg){
        this.code=code;
        this.msg=msg;

    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }}
