package come.point.mall.pointmallbackend.service.impl;

import com.alipay.api.domain.OrderItem;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import come.point.mall.pointmallbackend.common.OrderStatusEnum;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.dao.CarMapper;
import come.point.mall.pointmallbackend.dao.OrderMapper;
import come.point.mall.pointmallbackend.dao.ProductMapper;
import come.point.mall.pointmallbackend.pojo.Car;
import come.point.mall.pointmallbackend.pojo.Order;
import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.service.OrderService;
import come.point.mall.pointmallbackend.vo.OrderProductVo;
import come.point.mall.pointmallbackend.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

//    private static AlipayTradeService tradeService;


    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CarMapper carMapper;



    /**
     * 创建订单
     * @param userId
     * @param addressId
     * @param products
     * @return
     */
    @Override
    public ServerResponse createOrder(Integer userId, Integer addressId, List<OrderProductVo> products) {
        List<Order> orderList = this.assembleOrder(userId,addressId,products).getData();
        //生成订单
        orderMapper.batchInsert(orderList);
        //减少商品库存
        this.reduceProductStock(orderList);
        //清购物车
        List<String> productIds = new ArrayList<>();
        for (OrderProductVo product : products) {
            productIds.add(String.valueOf(product.getProductId()));
        }
        carMapper.deleteByUserIdProductIds(userId,productIds);
        return ServerResponse.createBySuccess();
    }

    /**
     * 更新订单状态
     * @param orderId
     * @param status
     * @return
     */
    @Override
    public ServerResponse updateOrderStatus(Integer orderId, Integer status) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(status);
        orderMapper.updateByPrimaryKeySelective(order);
        return null;
    }


    /**
     * 获取订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<Order> getOrderDetail(Integer userId,Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order != null) {
            return ServerResponse.createBySuccess(order);
        }
        return ServerResponse.createByErrorMessage("没有找到该订单");
    }

    /**
     * 获取订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        PageInfo pageResult = new PageInfo(orderList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse queryOrderPayStatus(Integer userId,Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        if(order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


    //backend
    @Override
    /**
     * 获取订单列表（后台）
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> manageList(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectAllOrder();
        PageInfo pageResult = new PageInfo(orderList);
        return ServerResponse.createBySuccess(pageResult);
    }


    /**
     * 获取订单详情（后台）
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<Order> manageDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            return ServerResponse.createBySuccess(order);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }



    /**
     * 发货
     * @param orderNo
     * @return
     */
    @Override
    public ServerResponse<String> mansgeSendGoods(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order != null) {
            if(order.getStatus() == OrderStatusEnum.PAID.getCode()) {
                order.setStatus(OrderStatusEnum.ORDER_SUCCESS.getCode());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.createBySuccess("发货成功");
            }
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public void closeOrder(int hour) {
        long closeDateTime = System.currentTimeMillis() / 1000 - 3600000;

        List<Order> orderList = orderMapper.selectOrderStatusByCreateTime(
                OrderStatusEnum.NO_PAY.getCode(),closeDateTime);
        for (Order order : orderList) {
            Integer stock = productMapper.selectStockByProductId(order.getProductId());
            //考虑到已生成的订单里的商品，被删除的情况
            if (stock == null) {
                continue;
            }
            Product product = new Product();
            product.setId(order.getProductId());
            product.setStock(stock + order.getProductNum());
            productMapper.updateByPrimaryKeySelective(product);
            order.setStatus(OrderStatusEnum.ORDER_CLOSE.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
            log.info("关闭订单 orderNo:{}",order.getOrderNo());
        }
    }

    /**
     * 生成订单
     * @param userId
     * @param addressId
     * @param productVos
     * @return
     */
    private ServerResponse<List<Order>> assembleOrder(Integer userId,Integer addressId,List<OrderProductVo> productVos) {
        List<Order> orderList = new ArrayList<>();
        for (OrderProductVo productVo : productVos) {
            Product product = productMapper.selectByPrimaryKey(productVo.getProductId());
            if (product == null) {
                return ServerResponse.createByErrorMessage("商品不存在");
            }
            //校验库存
            if (productVo.getProductNum() > product.getStock()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
            }
            Order order = new Order();
            long orderNo = this.generateOrderNo();
            order.setOrderNo(orderNo);//订单号
            order.setStatus(OrderStatusEnum.NO_PAY.getCode());//订单状态
            order.setUserId(userId);  //用户id
            order.setAddressId(addressId);//发货地址id
            order.setProductId(productVo.getProductId());
            order.setProductNum(productVo.getProductNum());
            orderList.add(order);
        }
        return ServerResponse.createBySuccess(orderList);
    }
    /**
     * 生成订单号
     * @return
     */
    private long generateOrderNo()  {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }


    /**
     * 减少商品库存
     * @param orderList
     */
    private void reduceProductStock(List<Order> orderList) {
        for(Order order:orderList) {
            Product product = productMapper.selectByPrimaryKey(order.getProductId());
            product.setStock(product.getStock() - order.getProductNum());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    /**
     * 清空购物车
     * @param carList
     */
    private void cleanCart(List<Car> carList) {
        for(Car car : carList) {
            carMapper.deleteByPrimaryKey(car.getId());
        }
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//    @Override
//    public ServerResponse pay(Long orderNo,Integer userId,String path) {
//        Map<String,String> resultMap = Maps.newHashMap();
//        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
//        if(order == null) {
//            return ServerResponse.createByErrorMessage("用户没有该订单");
//        }
//        resultMap.put("orderNo",String.valueOf(order.getOrderNo()));
//
//
//        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
//        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
//        String outTradeNo = order.getOrderNo().toString();
//
//        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
//        String subject = new StringBuilder().append("mmall扫码支付，订单号：").append(outTradeNo).toString();
//
//        // (必填) 订单总金额，单位为元，不能超过1亿元
//        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
//        String totalAmount = order.getProductNum().toString();
//
//        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
//        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
//        String undiscountableAmount = "0";
//
//        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
//        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
//        String sellerId = "";
//
//        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
//        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();
//
//        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
//        String operatorId = "test_operator_id";
//
//        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
//        String storeId = "test_store_id";
//
//        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088100200300400500");
//
//        // 支付超时，定义为120分钟
//        String timeoutExpress = "120m";
//
//        // 商品明细列表，需填写购买商品详细信息，
//        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
//
//        // 创建扫码支付请求builder，设置请求参数
//        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
//                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
//                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
//                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
//                .setTimeoutExpress(timeoutExpress)
//                .setGoodsDetailList(goodsDetailList);
//
//
//
//        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
//        return ServerResponse.createBySuccess(resultMap);
//    }
//
//    @Override
//    public ServerResponse aliCallback(Map<String, String> params) {
//        Long orderNo = Long.parseLong(params.get("out_trade_no"));  //商户订单号
//        String tradeNo = params.get("trade_no");//支付宝交易号
//        String tradeStatus = params.get("trade_status");//交易状态
//
//        Order order = orderMapper.selectByOrderNo(orderNo);
//        if(order == null) {
//            return ServerResponse.createByErrorMessage("非商城订单，回调忽略");
//        }
//        if(order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
//            return ServerResponse.createBySuccess("支付宝重复调用");
//        }
//        return null;
//    }
}
