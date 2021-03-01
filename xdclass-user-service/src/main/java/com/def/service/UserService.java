package com.def.service;

import com.def.config.JsonData;
import com.def.model.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.def.request.UserLoginRequest;
import com.def.request.UserRegisterRequest;
import com.def.vo.UserVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wr
 * @since 2021-02-20
 */
public interface UserService extends IService<UserDO> {

    JsonData registry(UserRegisterRequest request);

    JsonData login(UserLoginRequest request);

    UserVO getUserInfo();
}
