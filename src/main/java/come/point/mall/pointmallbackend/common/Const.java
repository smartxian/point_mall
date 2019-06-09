package come.point.mall.pointmallbackend.common;

public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public interface Cart {
        int CHECKED = 1;//即购物车选中状态
        int UN_CHECKED = 0;//购物车为选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL"; //限制失败
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS"; //限制成功
    }

    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

}
