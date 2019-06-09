package come.point.mall.pointmallbackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
    private Integer id;

    private Integer userId;

    private String receiverDesc;

    private Long createTime;

    private Long updateTime;
}