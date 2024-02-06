package org.example.redenvelope.service;

import org.example.redenvelope.pojo.RedPacketDto;

import java.math.BigDecimal;

/**
 * 创建人：Jason
 * 创建时间：2023/6/7
 * 描述你的类：
 */
public interface IRedPacketService {
    //发红包核心业务
    String handOut(RedPacketDto dto)throws Exception;
    //抢红包
    BigDecimal rob(Integer userId,String redId) throws Exception;

}
