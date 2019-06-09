package come.point.mall.pointmallbackend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import come.point.mall.pointmallbackend.common.ResponseCode;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.dao.ProductMapper;
import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 更新或新增商品
     * @param product
     * @return
     */
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product != null) {
            if(product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");
            } else {
                int rowCount = productMapper.insert(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品不正确");
    }

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize) {
        //startPage --start
        //填充自己的sql查询逻辑
        //pageHelper收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();

        PageInfo pageResult = new PageInfo(productList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        return null;
    }

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<Product> getProductDetail(Integer productId) {
        if(productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        return ServerResponse.createBySuccess(product);
    }

    @Override
    public ServerResponse<List<Product>> getProductByCategoryId(Integer categoryId, int start, int num) {
        return ServerResponse.createBySuccess(productMapper.selectByCategory(categoryId, start, num));
    }
}
