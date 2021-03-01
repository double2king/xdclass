package com.def.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.def.config.JsonData;
import com.def.enums.BizCodeEnum;
import com.def.enums.CouponPublishEnum;
import com.def.enums.CouponStateEnum;
import com.def.enums.CouponTypeEnum;
import com.def.exception.BizException;
import com.def.handlerInterceptor.TokenHandlerInterceptor;
import com.def.mapper.CouponMapper;
import com.def.mapper.CouponRecordMapper;
import com.def.model.CouponDO;
import com.def.model.CouponRecordDO;
import com.def.module.TokenInfo;
import com.def.service.CouponService;
import com.def.utils.PageUtil;
import com.def.vo.CouponVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wr
 * @since 2021-02-25
 */
@Service
@Slf4j
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponDO> implements CouponService {

    @Autowired
    CouponRecordMapper couponRecordMapper;

    @Autowired
    RedissonClient redissonClient;

    @Override
    /**
     * 获取优惠券列表
     * @param page
     * @return
     */
    public JsonData getCoupon(Page<CouponDO> page) {
        //查询分页数据
        Page<CouponDO> iPage = baseMapper.selectPage(page, new QueryWrapper<CouponDO>()
                .eq("publish", CouponPublishEnum.PUBLISH)
                .orderByDesc("create_time"));
        return JsonData.buildSuccess(PageUtil.getPage(iPage, CouponVO.class));
    }

    /**
     * 领取优惠券
     * 1.优惠券已经发布、储量大于0、且已在活动区间
     * 2.判断用户是否已经到达领取最大额度
     * 3.领取，扣减库存，同时增加领取记录
     * @param id
     * @param category
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonData receive(String id, CouponTypeEnum category) {

        QueryWrapper<CouponDO> condition = new QueryWrapper<CouponDO>().eq("id", id)
                .eq("category", category)
                .eq("publish", CouponPublishEnum.PUBLISH);

        TokenInfo tokenInfo = TokenHandlerInterceptor.userInfo.get();

        CouponDO couponDO = baseMapper.selectOne(condition);
        //检验是否符合领取条件
        check(couponDO, id, tokenInfo.getId());
        //扣减库存
        int reduce = baseMapper.reduce(id);
        if (reduce < 1) {
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
        //添加记录
        CouponRecordDO couponRecordDO = new CouponRecordDO();
        BeanUtils.copyProperties(couponDO, couponRecordDO);
        couponRecordDO.setCouponId(Long.parseLong(id));
        couponRecordDO.setCreateTime(new Date());
        couponRecordDO.setUseState(CouponStateEnum.NEW.name());
        couponRecordDO.setUserId(tokenInfo.getId());
        couponRecordDO.setUserName(tokenInfo.getName());
        couponRecordDO.setId(null);
        couponRecordMapper.insert(couponRecordDO);

        return JsonData.buildSuccess("领取成功");
    }

    /**
     * 1.优惠券已经发布、储量大于0、且已在活动区间
     * 2.判断用户是否已经到达领取最大额度
     * @param couponDO
     */
    public void check(CouponDO couponDO, String couponId, long userId) {

        if (couponDO == null) {
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }

        if (couponDO.getStock() <= 0) {
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }

        if (couponDO.getStartTime().getTime() > System.currentTimeMillis()
                || couponDO.getEndTime().getTime() < System.currentTimeMillis()) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
        }

        int count = couponRecordMapper.selectCount(new QueryWrapper<CouponRecordDO>().eq("coupon_id", couponId)
                .eq("user_id", userId));

        if (count >= couponDO.getUserLimit()) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }

    }
}
