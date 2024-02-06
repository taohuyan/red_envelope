package org.example.redenvelope.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 创建人：呼延涛
 * 创建时间：2023/6/7
 * 描述你的类：
 *
 */
public class RedPacketUtil {
    /**
     *
     * @param totalAmount:红包总金额【分】
     * @param totalPeopleAmountNum：红包总人数
     * @return：每一笔红包列表
     */
    public static List<Integer> divideRedPacket(Integer totalAmount,Integer totalPeopleAmountNum){

        //存储每次产生的小红包随机金额列表 单位为分
        List<Integer> amountList=new ArrayList();

        //判断总金额和总个数的合法性
        if(totalAmount>0&& totalPeopleAmountNum>0){
            //记录剩余的总金额
            Integer restAmount=totalAmount;
            //记录剩余的总人数
            Integer restPeopleNum=totalPeopleAmountNum;

            //定义产生随机数的实例对象。
            Random random=new Random();
            for(int i=0;i<totalPeopleAmountNum-1;i++){//0---8
                //随机范围(1,剩余人均金额的两倍]，左闭右开-aomout即为产生的随机金额
                //单位为分
                int amount=random.nextInt(restAmount/restPeopleNum*2-1)+1;
                //更新剩余的总金额 M=M-R;
                restAmount-=amount;
                //更新剩余的总人数
                restPeopleNum--;
                amountList.add(amount);
            }
            amountList.add(restAmount);//最后一笔红包随机金额 只要这样才能把前几笔+最后一笔=总金额

        }
        return amountList;

    }
}
