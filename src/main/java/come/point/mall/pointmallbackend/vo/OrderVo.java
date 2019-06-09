package come.point.mall.pointmallbackend.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderVo {
    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;  //支付方式

    private String paymentTypeDesc;  //支付方式描述

    private Integer postage;  //运费

    private Integer status;  //状态

    private String statusDesc; //状态描述

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    //订单明细
    private List<OrderItemVo> orderItemVoList;

    private String imageHost;

    private Integer shippingId;

    private String receiveName;

    private ShippingVo shippingVo;
}
