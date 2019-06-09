package come.point.mall.pointmallbackend.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.dao.CategoryMapper;
import come.point.mall.pointmallbackend.pojo.Category;
import come.point.mall.pointmallbackend.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加分类
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse addCategory(String categoryName,Integer parentId) {
        if(parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//分类可用

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }

        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    /**
     * 更新分类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName) {
        if(categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新分类名成功");
        }
        return ServerResponse.createByErrorMessage("更新分类名称失败");
    }

    /**
     * 获取当前分类的子分类
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)) {
//            logger.info("未找到当前分类的子分类");
            log.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点的id及子节点的id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null) {
            for (Category categoryItem:categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归算法算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null) {
            categorySet.add(category);
        }
        //查找子节点，递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem:categoryList) {
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
