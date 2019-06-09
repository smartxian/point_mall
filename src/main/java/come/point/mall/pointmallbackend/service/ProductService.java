package come.point.mall.pointmallbackend.service;

import com.github.pagehelper.PageInfo;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.vo.ProductDetailVo;

public interface ProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse setSaleStatus(Integer productId,Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
}
