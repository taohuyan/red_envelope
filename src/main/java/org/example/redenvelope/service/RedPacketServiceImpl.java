package org.example.redenvelope.service;

import org.example.redenvelope.controller.RedPacketController;
import org.example.redenvelope.mapper.RedRecordMapper;
import org.example.redenvelope.pojo.RedPacketDto;
import org.example.redenvelope.util.RedPacketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 创建人：Jason
 * 创建时间：2023/6/7
 * 描述你的类：
 */
@Service
public class RedPacketServiceImpl implements IRedPacketService {
    //定义日志
    private static final Logger log = LoggerFactory.getLogger(RedPacketController.class);

    @Autowired
    private RedRecordMapper redRecordMapper;

    @Autowired
    private RedServiceImpl redService;

    //存储缓存系统中的Key前缀
    private static final String keyPrefix = "redis:red:packet:";

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;//操作redis的对象

    @Override
    //发红包
    public String handOut(RedPacketDto dto) throws Exception {
        String redId = null;
        if (dto.getAmount().floatValue() > 0 && dto.getTotal() > 0) {//判断红包个数和面值是否合法
            //采用二倍均值法 随机产生金额。
            List<Integer> list = RedPacketUtil.divideRedPacket(dto.getAmount().intValue(), dto.getTotal());
            //生成红包全局唯一标识符号
            String time = String.valueOf(System.nanoTime());
            //根据缓存Key的前缀与其他信息拼接成一个新的用于存储金额列表的key
            redId = new StringBuffer(keyPrefix).append(dto.getUserId()).append(":").append(time).toString().trim();
            //将随机金额存储到redis缓存list中
            redisTemplate.opsForList().leftPushAll(redId, list.toArray());
            //将红包的总个数存储到redis中
            String redisTotalKey = redId + ":total";
            redisTemplate.opsForValue().set(redisTotalKey, dto.getTotal());
            //把数据存储到redis_detail mysql中

            redService.recordRedPacket(dto, redId, list);
        }

        return redId;
    }

    @Override
    public BigDecimal rob(Integer userId, String redId) throws Exception {
        //把抢的红包存储到redis缓存里
        //使用redis中的字符串类型进行存储
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //当前用户是否已经抢了该红包，
        Object obj = valueOperations.get(redId + userId + ":rob");
        if (obj != null) {//该用户已经抢完了该红包
            return new BigDecimal(obj.toString());
        }
        BigDecimal result = null;
        //没有抢过红包
        boolean res = click(redId);
        if (res) {//能抢红包
            //上分布式锁 一个红包每个人只能抢到一次随机金额，
            final String lockKey = redId + userId + "-lock";
            //调用setIfAbsent方法，实现了分布式锁
            boolean lock = valueOperations.setIfAbsent(lockKey, redId);

            redisTemplate.expire(lockKey, 24L, TimeUnit.HOURS);

            if (lock) {//如果加锁成功
                //红包金额value
                Object value = redisTemplate.opsForList().rightPop(redId);
                if (value != null) {
                    //把红包总个数减1
                    String redTotalKey = redId + ":total";
                    valueOperations.decrement(redTotalKey);

                    //将红包金额返回给用户的同时，将抢红包记录记入到数据库中
                    //最终转换成元进行存储
                    result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
                    //红包进入到数据库中
                    redService.recordRobRedPacket(userId, redId, result);
                    //红包进入到缓存里
                    valueOperations.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);
                    //填充好日志
                    log.info("当前用户抢到了红包：userId={} key={} 金额={}", userId, redId, result);

                }
            }

        }
        return result;
    }

    //根据红包id进行检索 该用户是否能对该红包进行抢
    private boolean click(String redId) {
        //定义Redis的Bean操作-值
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //查询缓存中红包的剩余个数
        String redTotalKey = redId + ":total";
        Object total = valueOperations.get(redTotalKey);
        if (total != null && Integer.valueOf(total.toString()) > 0) {//返回为true，代表有红包，可以抢
            return true;
        }
        return false;

    }


}
