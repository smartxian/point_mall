package come.point.mall.pointmallbackend.vo;

import lombok.Data;


@Data
public class CarProductVo {
    //结合了产品和购物车的一个抽象对象
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;//购物车中此商品的数量
    private String productName;
    private String productSubTitle;
    private String productMainImage;
    private Long productPrice;
//    private Integer productStatus;
    private Long productTotalPrice;
    private Integer productStock;
    private Integer productChecked;//此商品是否勾选

    private String limitQuantity;//限制数量的一个返回结果
}
