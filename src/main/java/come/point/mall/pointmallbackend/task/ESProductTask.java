package come.point.mall.pointmallbackend.task;

import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.service.ProductESService;
import come.point.mall.pointmallbackend.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@EnableScheduling
public class ESProductTask {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductESService productESService;

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void productDataTask() {
        List<Product> productList = productService.getProductList(1, 100000000).getData().getList();
        productList.forEach((e) -> {
            productESService.index(e);
        });
    }
}
