package come.point.mall.pointmallbackend.vo;

import java.math.BigDecimal;
import java.util.List;

public class CarVo {
    private List<CarProductVo> carProductVoList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;//是否已经都勾选
    private String imageHost;

    public List<CarProductVo> getCarProductVoList() {
        return carProductVoList;
    }

    public void setCarProductVoList(List<CarProductVo> carProductVoList) {
        this.carProductVoList = carProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
