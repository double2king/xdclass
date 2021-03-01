package com.def.controller;


import com.def.config.JsonData;
import com.def.request.UserLoginRequest;
import com.def.request.UserRegisterRequest;
import com.def.service.UserService;
import com.def.vo.UserVO;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wr
 * @since 2021-02-20
 */
@RestController
@RequestMapping("/api/user/v1")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    @ApiOperation("注册用户")
    public JsonData addUser(@ApiParam("用户注册对象") @RequestBody UserRegisterRequest registerRequest) {
        return userService.registry(registerRequest);
    }

    @PostMapping("/login")
    @ApiOperation("登录用户")
    public JsonData login(@ApiParam("用户登录对象") @RequestBody UserLoginRequest loginRequest) {

        if (StringUtils.isBlank(loginRequest.getMail()) || StringUtils.isBlank(loginRequest.getPwd())) {
            return JsonData.buildError("邮箱和密码不能为空");
        }

        return userService.login(loginRequest);
    }

    @GetMapping("/userInfo")
    @ApiOperation("用户信息")
    @ApiResponses(
            @ApiResponse(code= 200, response = UserVO.class, message = "ok,明细参考用户展示类")
    )
    public JsonData<UserVO> userInfo() {
        UserVO userInfo = userService.getUserInfo();
        return JsonData.buildSuccess(userInfo);
    }


}

