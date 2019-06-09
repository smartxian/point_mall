package come.point.mall.pointmallbackend.service;

import com.github.pagehelper.PageInfo;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.Product;

import java.util.List;

public interface ProductService {
    /**
     * 创建或编辑商品
     * @param product
     * @return
     */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    ServerResponse<Product> getProductDetail(Integer productId);

    ServerResponse<List<Product>> getProductByCategoryId(Integer categoryId, int start, int num);
}
