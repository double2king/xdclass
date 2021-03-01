package com.def.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.def.config.JsonData;
import com.def.enums.SendEnum;
import com.def.model.UserDO;
import com.def.service.NotifyService;
import com.def.service.UserService;
import com.def.util.CommonUtil;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/notify/v1")
@Api(tags = "验证码")
@Slf4j
public class NotifyController {

    @Autowired
    Producer kaptchaProducer;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    NotifyService notifyService;

    @Autowired
    UserService userService;

    private final int time = 1000 * 60 * 5;

    @ApiOperation("获取验证码图片")
    @GetMapping("/kaptcha")
    public String kaptcha(HttpServletRequest request, HttpServletResponse response) {

        String key = getKaptchaCacheKey(request);
        //生成校验码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        try (ServletOutputStream os = response.getOutputStream()) {
            ImageIO.write(image, "jpg", os);
        } catch (Exception e) {
            log.info("验证码服务报错{}", e);
        }
        //存入redis
        redisTemplate.opsForValue().set(key, text, time, TimeUnit.MILLISECONDS);
        return "success";
    }

    @ApiOperation("发送邮件")
    @GetMapping("/send")
    public JsonData sendEmail(@ApiParam("发送邮箱") String to,
                              @ApiParam("验证码") String code,
                              HttpServletRequest request) {
        String key;
        String cacheCode;
        //验证邮箱是否合法
        if (!CommonUtil.isEmail(to)) {
            return JsonData.buildError("邮箱格式错误");
        }

        key = getKaptchaCacheKey(request);
        cacheCode = redisTemplate.opsForValue().get(key);
        //判断验证码是否正确
        if (StringUtils.isBlank(cacheCode)) {
            return JsonData.buildError("验证码已过期，请刷新验证码");
        } else if(!cacheCode.equalsIgnoreCase(code)) {
            return JsonData.buildError("验证码错误");
        }
        //邮箱是否已被注册
        UserDO email = userService.getOne(new QueryWrapper<UserDO>().eq("mail", to));
        if (email != null) {
            return JsonData.buildError("邮箱已存在");
        }
        //删除redis验证码，发送邮箱
        if (!redisTemplate.delete(key)) {
            return JsonData.buildError("请勿重复点击");
        }

        return notifyService.send(SendEnum.USER_EMAIL, to);
    }

    public String getKaptchaCacheKey(HttpServletRequest request) {
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("user-agent");
        log.info("ip:{},user-agent:{}", ip, userAgent);
        return "xdclass-user-service:kaptcha:" + CommonUtil.MD5(ip + userAgent);
    }



}
