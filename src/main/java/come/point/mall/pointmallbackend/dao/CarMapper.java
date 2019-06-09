package come.point.mall.pointmallbackend.dao;

import come.point.mall.pointmallbackend.pojo.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
@MapperScan
public interface CarMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Car record);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    Car selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Car> selectCarByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);

    int selectCartProductCount(@Param("userId") Integer userId);
}