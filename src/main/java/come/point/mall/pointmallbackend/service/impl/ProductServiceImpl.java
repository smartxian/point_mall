package come.point.mall.pointmallbackend.service.impl;

import com.github.pagehelper.PageInfo;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.service.ProductService;
import come.point.mall.pointmallbackend.vo.ProductDetailVo;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        return null;
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        return null;
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        return null;
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        return null;
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        return null;
    }
}
