package come.point.mall.pointmallbackend.service;

import com.github.pagehelper.PageInfo;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.Order;
import come.point.mall.pointmallbackend.vo.OrderProductVo;
import come.point.mall.pointmallbackend.vo.OrderVo;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 创建订单
     * @param userId
     * @param addressId
     * @return
     */
    ServerResponse createOrder(Integer userId, Integer addressId, List<OrderProductVo> products);

    /**
     * 变更订单状态
     * @param orderId
     * @param status
     * @return
     */
    ServerResponse updateOrderStatus(Integer orderId,Integer status);

    /**
     * 获取订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<Order> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 获取订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    /**
     * 查询订单状态
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);


    //backend
    /**
     * 获取订单列表（管理员)
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);

    /**
     * 获取订单详情（管理员）
     * @param orderNo
     * @return
     */
    ServerResponse<Order> manageDetail(Long orderNo);

    /**
     * 发货
     * @param orderNo
     * @return
     */
    ServerResponse<String> mansgeSendGoods(Long orderNo);

    /**
     * hour个小时之内未付款的订单进行关闭
     * @param hour
     */
    void closeOrder(int hour);


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    /**
//     * 支付
//     * @param orderNo
//     * @param userId
//     * @param path
//     * @return
//     */
//    ServerResponse pay(Long orderNo, Integer userId, String path);
//
//    /**
//     * 回调
//     * @param params
//     * @return
//     */
//    ServerResponse aliCallback(Map<String,String> params);
}
