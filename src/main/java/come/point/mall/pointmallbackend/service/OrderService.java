package come.point.mall.pointmallbackend.service;

import com.github.pagehelper.PageInfo;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.vo.OrderVo;

import java.util.Map;

public interface OrderService {
    /**
     * 支付
     * @param orderNo
     * @param userId
     * @param path
     * @return
     */
    ServerResponse pay(Long orderNo, Integer userId, String path);

    /**
     * 回调
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String,String> params);

    /**
     * 查询订单状态
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    /**
     * 创建订单
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse createOrder(Integer userId,Integer shippingId);

    /**
     * 取消订单
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<String> cancel(Integer userId,Long orderNo);

    /**
     * 获取购物车中商品信息
     * @param uesrId
     * @return
     */
    ServerResponse getOrderCartProduct(Integer uesrId);

    /**
     * 获取订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 获取订单列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


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
    ServerResponse<OrderVo> manageDetail(Long orderNo);

    /**
     * 按订单号搜索
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

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
}
