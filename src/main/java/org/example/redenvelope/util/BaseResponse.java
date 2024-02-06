package org.example.redenvelope.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 创建人：呼延涛
 * 创建时间：2023/6/7
 * 描述你的类：
 * 响应给前端的公共基类
 */
@Data
@ToString
@NoArgsConstructor

public class BaseResponse<T> {
    //状态码
    private Integer code;
    //描述信息
    private String msg;
    //响应数据
    private T data;


    public BaseResponse(StatusCode statusCode){
        this.code=statusCode.getCode();
        this.msg=statusCode.getMsg();
    }
    public BaseResponse(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public BaseResponse(Integer code,String msg,T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

}
