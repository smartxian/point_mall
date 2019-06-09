package come.point.mall.pointmallbackend.controller.backend;

import come.point.mall.pointmallbackend.common.Const;
import come.point.mall.pointmallbackend.common.ResponseCode;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.Product;
import come.point.mall.pointmallbackend.pojo.User;
import come.point.mall.pointmallbackend.service.ProductESService;
import come.point.mall.pointmallbackend.service.ProductService;
import come.point.mall.pointmallbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductESService productESService;

    @Autowired
    private UserService userService;
    /**
     * 新增商品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请以管理员身份登录");

        }
        if(userService.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            productESService.index(product);
            return productService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    /**
     * 后台查询产品list
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请以管理员身份登录");
        }
        if(userService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createBySuccess((Product)productService.getProductList(pageNum,pageSize).getData().getList());
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

//    /**
//     * 后台商品搜索功能
//     * @param session
//     * @param productName
//     * @param productId
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @RequestMapping("search.do")
//    @ResponseBody
//    public ServerResponse productSearch(HttpSession session, String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if(user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请以管理员身份登录");
//
//        }
//        if(userService.checkAdminRole(user).isSuccess()) {
//            return productService.searchProduct(productName,productId,pageNum,pageSize);
//        } else {
//            return ServerResponse.createByErrorMessage("无权限操作");
//        }
//    }


}
