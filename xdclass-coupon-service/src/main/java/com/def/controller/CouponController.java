package com.def.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.def.config.JsonData;
import com.def.enums.CouponTypeEnum;
import com.def.model.CouponDO;
import com.def.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wr
 * @since 2021-02-25
 */
@RestController
@RequestMapping("/api/coupon/v1")
@Api(tags = "优惠券服务", description = "优惠券服务")
public class CouponController {

    @Autowired
    CouponService couponService;

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/getList")
    @ApiOperation("获取优惠券列表")
    public JsonData getCoupon(
            @ApiParam(value = "当前页", defaultValue = "1") @RequestParam(defaultValue = "1") int cur,
            @ApiParam(value = "页大小", defaultValue = "10") @RequestParam(defaultValue = "10") int size
    ){
        Page<CouponDO> page = new Page<>(cur, size);
        return couponService.getCoupon(page);
    }

    @GetMapping("/receive")
    @ApiOperation("领取优惠券")
    public JsonData receive(@ApiParam("优惠券id") String id){
        
        RLock lock = redissonClient.getLock("coupon:" + id);
        lock.lock();
        try {
            return couponService.receive(id, CouponTypeEnum.PROMOTION);
        } finally {
            lock.unlock();
        }

    }

}

