package come.point.mall.pointmallbackend.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import come.point.mall.pointmallbackend.common.Const;
import come.point.mall.pointmallbackend.common.ResponseCode;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.dao.CarMapper;
import come.point.mall.pointmallbackend.dao.ProductMapper;
import come.point.mall.pointmallbackend.pojo.Car;
import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.service.CarService;
import come.point.mall.pointmallbackend.utils.BigDecimalUtil;
import come.point.mall.pointmallbackend.vo.CarProductVo;
import come.point.mall.pointmallbackend.vo.CarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private ProductMapper productMapper;
    /**
     * 添加购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse<CarVo> add(Integer userId, Integer productId, Integer count) {
        //参数不正确
        if(productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Car cart = carMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null) {
            //这个产品不再这个购物车里，需要新增一个这个产品的记录
            Car cartItem = new Car();
            cartItem.setQuantity(count);  //购物车数量
            cartItem.setChecked(Const.Cart.CHECKED);  //购物车状态
            cartItem.setProductId(productId);  //商品id
            cartItem.setUserId(userId);  //用户id

            carMapper.insert(cartItem);
        } else {
            //这个产品已经在购物车了
            //如果产品已存在，数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            carMapper.updateByPrimaryKeySelective(cart);
        }
        CarVo carVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(carVo);
    }


    /**
     * 更新购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @Override
    public ServerResponse<CarVo> update(Integer userId, Integer productId, Integer count) {
        //参数不正确
        if(productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Car car = carMapper.selectCartByUserIdProductId(userId,productId);
        if(car != null) {
            car.setQuantity(count);
        }
        carMapper.updateByPrimaryKeySelective(car);
        CarVo carVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(carVo);
    }


    /**
     * 删除购物车中产品
     * @param userId
     * @param productIds
     * @return
     */
    @Override
    public ServerResponse<CarVo> deleteProduct(Integer userId, String productIds) {
        //用","分隔字符串，自动添加到集合中
        List<String> productList  = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        carMapper.deleteByUserIdProductIds(userId,productList);
        CarVo carVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(carVo);
    }


    /**
     * 列举购物车中的内容
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<CarVo> list(Integer userId) {
        CarVo carVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(carVo);
    }

    /**
     * 全选或全不选
     * @param userId
     * @param checked
     * @return
     */
    @Override
    public ServerResponse<CarVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        carMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }


    /**
     * 获取购物车中商品数量
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(carMapper.selectCartProductCount(userId));
    }


    /**
     * 重新计算，做各种校验
     * @param userId
     * @return
     */
    private CarVo getCartVoLimit(Integer userId) {
        CarVo carVo = new CarVo();
        List<Car> cartList = carMapper.selectCarByUserId(userId);

        List<CarProductVo> carProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if(CollectionUtils.isNotEmpty(cartList)) {
            for (Car cartItem : cartList) {
                CarProductVo carProductVo = new CarProductVo();
                carProductVo.setId(cartItem.getId());
                carProductVo.setUserId(userId);
                carProductVo.setProductId(cartItem.getProductId());
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

                if(product != null) {
                    carProductVo.setProductMainImage(product.getMainImage());
                    carProductVo.setProductName(product.getName());
                    carProductVo.setProductSubTitle(product.getSubtitle());
//                    carProductVo.setProductStatus(product.getStatus());
                    carProductVo.setProductPrice(product.getPrice());
                    carProductVo.setProductStock(product.getStock());
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()) {
                        //库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        //产品的库存大于购物车中商品的数量
                        carProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        carProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车中更新有效库存
                        Car cartForQuantity = new Car();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        carMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    carProductVo.setQuantity(buyLimitCount);
                    //计算总价
//                    carProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),carProductVo.getQuantity()));//某一产品的总价
                    carProductVo.setProductTotalPrice(Math.multiplyExact(product.getPrice(), carProductVo.getQuantity()));
                    carProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked() == Const.Cart.CHECKED) {
                    //如果已经勾选，增加到整个购物车总价值中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), carProductVo.getProductTotalPrice().doubleValue());
                }
                carProductVoList.add(carProductVo);
            }
        }
        carVo.setCartTotalPrice(cartTotalPrice);
        carVo.setCarProductVoList(carProductVoList);
        carVo.setAllChecked(this.getAllCheckedStatus(userId));
//        carVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return carVo;
    }


    /**
     * 判断是否全选
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId) {
        if(userId == null) {
            return false;
        }
        return carMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

}
