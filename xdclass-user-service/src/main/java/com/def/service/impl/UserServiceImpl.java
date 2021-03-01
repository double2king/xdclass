package com.def.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.def.config.InterceptorConfig;
import com.def.config.JsonData;
import com.def.exception.BizException;
import com.def.handlerInterceptor.TokenHandlerInterceptor;
import com.def.model.UserDO;
import com.def.mapper.UserMapper;
import com.def.module.TokenInfo;
import com.def.request.UserLoginRequest;
import com.def.request.UserRegisterRequest;
import com.def.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.def.util.CommonUtil;
import com.def.utils.TokenUtils;
import com.def.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wr
 * @since 2021-02-20
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    StringRedisTemplate redisTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData registry(UserRegisterRequest request) {

        String key = "user_service:email:" + request.getMail();
        String val = redisTemplate.opsForValue().get(key);
        //判断验证码是否有效
        if (StringUtils.isBlank(val)) {
            return JsonData.buildError("邮箱验证码已经失效，请重新注册");
        }
        String code = val.split("_")[0];
        //校验验证码是否相同
        if (!code.equalsIgnoreCase(request.getCode())) {
            return JsonData.buildError("验证码错误");
        }
        //插入数据库
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(request, userDO);
        //重置密码和盐
        userDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));
        String cryptPwd = Md5Crypt.md5Crypt(userDO.getPwd().getBytes(), userDO.getSecret());
        userDO.setPwd(cryptPwd);
        try {
            baseMapper.insert(userDO);
        } catch (DuplicateKeyException e) {
            return JsonData.buildError("邮箱已被注册");
        }
        //删除邮箱验证码
        redisTemplate.delete(key);
        return JsonData.buildSuccess();
    }

    @Override
    public JsonData login(UserLoginRequest request) {
        String mail = request.getMail();
        String pwd = request.getPwd();
        UserDO userDO = baseMapper.selectOne(new QueryWrapper<UserDO>().eq("mail", mail));
        if (userDO == null) {
            return JsonData.buildError("账号或密码错误");
        }
        String crptPwd = Md5Crypt.md5Crypt(pwd.getBytes(), userDO.getSecret());
        if (crptPwd.equals(userDO.getPwd())) {

            //生成token
            TokenInfo info = new TokenInfo();
            BeanUtils.copyProperties(userDO, info);
            return JsonData.buildSuccess(TokenUtils.geneJsonWebToken(info));

        } else {
            return JsonData.buildError("账号或密码错误");
        }
    }

    @Override
    public UserVO getUserInfo() {

        TokenInfo tokenInfo = TokenHandlerInterceptor.userInfo.get();

        UserDO userDO = baseMapper.selectById(tokenInfo.getId());

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDO, userVO);
        return userVO;
    }
}
