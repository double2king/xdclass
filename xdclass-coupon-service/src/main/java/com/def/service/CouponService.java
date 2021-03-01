package com.def.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.def.config.JsonData;
import com.def.enums.CouponTypeEnum;
import com.def.model.CouponDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.def.vo.CouponVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wr
 * @since 2021-02-25
 */
public interface CouponService extends IService<CouponDO> {
    /**
     * 获取优惠券列表
     * @param page
     * @return
     */
    JsonData getCoupon(Page<CouponDO> page);

    /**
     * 领取优惠券
     * @param id
     * @param category
     * @return
     */
    JsonData receive(String id, CouponTypeEnum category);

}
