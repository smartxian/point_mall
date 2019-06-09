package come.point.mall.pointmallbackend.service.impl;

import come.point.mall.pointmallbackend.common.Const;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.dao.UserMapper;
import come.point.mall.pointmallbackend.pojo.User;
import come.point.mall.pointmallbackend.service.UserService;
import come.point.mall.pointmallbackend.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user  = userMapper.selectLogin(username,md5Password);

        if(user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);

    }

    /**
     * 注册
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user) {

        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }


        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateInformation(User user) {
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        return null;
    }

    /**
     * 校验用户名邮箱是否有效
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str,String type) {
        if(StringUtils.isNotBlank(type)) {
            //开始校验
            if(Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);

                if(resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }
}
