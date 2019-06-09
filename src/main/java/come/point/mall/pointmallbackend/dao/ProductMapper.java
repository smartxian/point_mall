package come.point.mall.pointmallbackend.dao;

import come.point.mall.pointmallbackend.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
@MapperScan
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    Integer selectStockByProductId(Integer id);

    List<Product> selectByCategory(@Param("categoryId") Integer categoryId, @Param("start")int start, @Param("num")int num);
}