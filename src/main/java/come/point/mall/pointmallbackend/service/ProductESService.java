package come.point.mall.pointmallbackend.service;

import come.point.mall.pointmallbackend.pojo.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductESService extends ElasticsearchRepository<Product, Integer> {

}
