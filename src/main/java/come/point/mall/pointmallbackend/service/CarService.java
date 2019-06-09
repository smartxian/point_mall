package come.point.mall.pointmallbackend.service;

import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.vo.CarVo;

public interface CarService {
    ServerResponse<CarVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CarVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CarVo> deleteProduct(Integer userId, String productIds);

    ServerResponse<CarVo> list(Integer userId);

    ServerResponse<CarVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
