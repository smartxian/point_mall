package come.point.mall.pointmallbackend.controller.portal;

import com.github.pagehelper.PageInfo;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.service.ProductESService;
import come.point.mall.pointmallbackend.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductESService productESService;

    /**
     * 商品详情
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<Product> detail(Integer productId) {
        return productService.getProductDetail(productId);
    }

    /**
     * 产品搜索
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<List<Product>> list(@RequestParam(value = "keyword",required = false) String keyword,
                                              @RequestParam(value = "categoryId",required = false) Integer categoryId,
                                              @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        if(StringUtils.isEmpty(keyword) && categoryId == null) {
            return ServerResponse.createByErrorMessage("啥都没有查啥?");
        }
        if(!StringUtils.isEmpty(keyword)) {
            Pageable pageable = new PageRequest(pageNum, pageSize);
            // 分数，并自动按分排序
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                    .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name", keyword)),
                            ScoreFunctionBuilders.weightFactorFunction(1000)) // 权重：name 1000分
                    .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("message", keyword)),
                            ScoreFunctionBuilders.weightFactorFunction(100)); // 权重：message 100分
            // 分数、分页
            SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                    .withQuery(functionScoreQueryBuilder).build();
            Page<Product> searchPageResults = productESService.search(searchQuery);
            return ServerResponse.createBySuccess(searchPageResults.getContent());
        }
        if(categoryId != null) {
            return productService.getProductByCategoryId(categoryId, (pageNum - 1) * pageSize, pageSize);
        }
        return ServerResponse.createByError();

    }
}
