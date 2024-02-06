package org.example.redenvelope.service;

import org.example.redenvelope.pojo.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建人：Jason
 * 创建时间：2023/6/7
 * 描述你的类：
 */
public interface IRedService {

    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list);
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount);

}
