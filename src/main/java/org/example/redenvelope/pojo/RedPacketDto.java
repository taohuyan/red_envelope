package org.example.redenvelope.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建人：Jason
 * 创建时间：2023/6/7
 * 描述你的类：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RedPacketDto implements Serializable {
    private Integer userId;



    private Integer total;//指定有多少人抢

    private BigDecimal amount;//红包面值
}
