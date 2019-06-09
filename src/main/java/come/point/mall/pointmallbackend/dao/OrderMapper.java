package come.point.mall.pointmallbackend.dao;

import come.point.mall.pointmallbackend.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
@MapperScan
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    void batchInsert(@Param("orderList") List<Order> orderList);

    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId,@Param("orderNo") Long orderNo);

    List<Order> selectByUserId(Integer userId);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectAllOrder();

    List<Order> selectOrderStatusByCreateTime(@Param("status") Integer status,@Param("time") long time);

}