package come.point.mall.pointmallbackend.common;

public enum OrderStatusEnum {
    NO_PAY(10,"未支付"),
    PAID(20,"已付款"),
    ORDER_SUCCESS(40,"订单完成"),
    ORDER_CLOSE(50,"订单关闭");

    OrderStatusEnum(int code,String value) {
        this.value = value;
        this.code = code;
    }

    private String value;
    private int code;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static OrderStatusEnum codeOf(int code) {
        for(OrderStatusEnum orderStatusEnum : values()) {
            if(orderStatusEnum.code == code) {
                return orderStatusEnum;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }
}
