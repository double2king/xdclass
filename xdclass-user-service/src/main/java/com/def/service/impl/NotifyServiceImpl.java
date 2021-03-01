package com.def.service.impl;

import com.def.config.JsonData;
import com.def.enums.SendEnum;
import com.def.service.MailService;
import com.def.service.NotifyService;
import com.def.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private MailService mailService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public JsonData send(SendEnum type, String to) {

        String key;

        if (type == SendEnum.USER_EMAIL) {

            key = "user_service:email:" + to;
            String value = redisTemplate.opsForValue().get(key);
            String newValue = CommonUtil.getStringNumRandom(4) +  "_" + System.currentTimeMillis();
            //未被调用，直接发送
            if (StringUtils.isBlank(value)) {
                //重复发送
                if (!redisTemplate.opsForValue().setIfAbsent(key, newValue, 15, TimeUnit.MINUTES)) {
                    return JsonData.buildError("该邮箱发送过于频繁,一分钟后再试");
                }
            }else {
                long diff = System.currentTimeMillis() - Long.parseLong(value.split("_")[1]);
                if (diff < 60 * 1000) {
                    return JsonData.buildError("该邮箱发送过于频繁,一分钟后再试");
                }
                redisTemplate.opsForValue().set(key, newValue, 15, TimeUnit.MINUTES);
            }
            mailService.sendEmail(to, "验证码", "您的验证码是" + newValue.split("_")[0]);
            return JsonData.buildError("发送成功");
        }

        return null;
    }
}
