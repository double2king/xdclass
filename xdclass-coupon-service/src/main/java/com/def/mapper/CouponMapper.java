package com.def.mapper;

import com.def.model.CouponDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wr
 * @since 2021-02-25
 */
public interface CouponMapper extends BaseMapper<CouponDO> {

    @Update("update coupon set stock = stock - 1 where id = #{id} and stock > 0")
    int reduce(String id);

}
