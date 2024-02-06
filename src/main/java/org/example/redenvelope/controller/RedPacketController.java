package org.example.redenvelope.controller;

import org.example.redenvelope.pojo.RedPacketDto;
import org.example.redenvelope.service.IRedPacketService;
import org.example.redenvelope.util.BaseResponse;
import org.example.redenvelope.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 创建人：呼延涛
 * 创建时间：2023/6/7
 * 描述你的类：
 */
@RestController //Restful doGet doPost doDelete doPut
public class RedPacketController {

    //定义日志
    private static final Logger log= LoggerFactory.getLogger(RedPacketController.class);

    //定义红包请求路径

    private static final String prefix="red/packet";

    //定义红包业务逻辑处理服务接口
    @Autowired
    private IRedPacketService redPacketService;


    //发红包

    @PostMapping(value =prefix+"/hand/out",
            //限制前端必须发送JSON格式，我才能对你进行处理
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public BaseResponse handOut(@Validated @RequestBody RedPacketDto dto, BindingResult result){
        //参数校验
        if(result.hasErrors()){//参数有错误

            return new BaseResponse(StatusCode.InvalidPrams);
        }
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try{
            String redid=redPacketService.handOut(dto);
            response.setData(redid);

        }catch(Exception e){
            log.error("发红包异常：dto={}",dto,e.fillInStackTrace());
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
    //抢红包
    @GetMapping(value=prefix+"/rob")
    public BaseResponse rob(@RequestParam Integer userId,@RequestParam String redId){

        //定义响应对象
        BaseResponse response=new BaseResponse(StatusCode.Success);
        try{
            BigDecimal result=redPacketService.rob(userId,redId);
            if(result!=null){
                response.setData(result);
            }

        }catch(Exception e){
            log.error("抢红包异常");
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }

        return response;
    }

}
