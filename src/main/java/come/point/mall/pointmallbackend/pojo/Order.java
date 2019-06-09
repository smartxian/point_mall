package come.point.mall.pointmallbackend.pojo;


import lombok.Data;

@Data
public class Order {

    private Integer id;

    private Long orderNo;

    private Integer userId;

    private Integer addressId;

    private Integer productId;

    private Integer productNum;

    private Integer status;

    private Long createTime;

    private Long updateTime;
}