package org.example.redenvelope.service;

import com.weikun.main.mapper.RedDetailMapper;
import com.weikun.main.mapper.RedRecordMapper;
import com.weikun.main.mapper.RedRobRecordMapper;
import com.weikun.main.model.RedDetail;
import com.weikun.main.model.RedPacketDto;
import com.weikun.main.model.RedRecord;
import com.weikun.main.model.RedRobRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 创建人：Jason
 * 创建时间：2023/6/7
 * 描述你的类：
 */
@Service
public class RedServiceImpl implements IRedService {

    @Autowired
    private RedRecordMapper mapper;

    @Autowired
    private RedDetailMapper redDetailMapper;

    @Autowired
    private RedRobRecordMapper redRobRecordMapper;

    @Async//异步更新
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) {
        RedRecord redRecord=new RedRecord();
        redRecord.setUserId(dto.getUserId());
        redRecord.setAmount(dto.getAmount());
        redRecord.setCreateTime(new Date());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        mapper.insertSelective(redRecord);

        for(Integer i:list){
            RedDetail detail=new RedDetail();
            detail.setRecordId(redRecord.getId());//主表的主键
            detail.setAmount(BigDecimal.valueOf(i));//每一笔红包
            detail.setCreateTime(new Date());
            redDetailMapper.insertSelective(detail);
        }

    }
    @Async//异步更新
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) {
        RedRobRecord redRobRecord=new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redId);
        redRobRecord.setRobTime(new Date());
        redRobRecord.setAmount(amount);
        //将数据进入到mysql数据库
        redRobRecordMapper.insertSelective(redRobRecord);
    }
}
