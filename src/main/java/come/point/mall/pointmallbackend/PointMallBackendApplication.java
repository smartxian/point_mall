package come.point.mall.pointmallbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

@SpringBootApplication
public class PointMallBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(PointMallBackendApplication.class, args);
    }

}
