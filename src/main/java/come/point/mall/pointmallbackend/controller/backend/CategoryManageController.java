package come.point.mall.pointmallbackend.controller.backend;

import come.point.mall.pointmallbackend.common.Const;
import come.point.mall.pointmallbackend.common.ResponseCode;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.User;
import come.point.mall.pointmallbackend.service.CategoryService;
import come.point.mall.pointmallbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 添加商品分类
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //增加处理分类的逻辑
            return categoryService.addCategory(categoryName,parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员执行");
        }
    }

    /**
     * 更新分类名称
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "update_category.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()) {
            //更新categoryName
            return categoryService.updateCategoryName(categoryId,categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员执行");
        }
    }

    /**
     * 获取当前分类的子分类
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()) {
            //查询字节点的category信息，并且不递归，保证平级
            return categoryService.getChildParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员执行");
        }
    }

    /**
     * 获取当前节点的id及递归字节点的id
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(userService.checkAdminRole(user).isSuccess()) {
            //查询当前节点的id和递归子节点的id
            return categoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员执行");
        }
    }
}
