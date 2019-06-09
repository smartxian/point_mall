package come.point.mall.pointmallbackend.service.impl;

import com.github.pagehelper.PageInfo;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.service.OrderService;
import come.point.mall.pointmallbackend.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public ServerResponse pay(Long orderNo, Integer userId, String path) {
        return null;
    }

    @Override
    public ServerResponse aliCallback(Map<String, String> params) {
        return null;
    }

    @Override
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo) {
        return null;
    }

    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        return null;
    }

    @Override
    public ServerResponse<String> cancel(Integer userId, Long orderNo) {
        return null;
    }

    @Override
    public ServerResponse getOrderCartProduct(Integer uesrId) {
        return null;
    }

    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        return null;
    }

    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ServerResponse<PageInfo> manageList(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ServerResponse<OrderVo> manageDetail(Long orderNo) {
        return null;
    }

    @Override
    public ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ServerResponse<String> mansgeSendGoods(Long orderNo) {
        return null;
    }

    @Override
    public void closeOrder(int hour) {

    }
}
