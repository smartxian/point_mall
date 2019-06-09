package come.point.mall.pointmallbackend.service;

import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.User;

public interface UserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse checkAdminRole(User user);
}
