package come.point.mall.pointmallbackend.task;

import come.point.mall.pointmallbackend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class CloseOrderTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTask() {
        int hour = 1;
        orderService.closeOrder(hour);
    }
}
