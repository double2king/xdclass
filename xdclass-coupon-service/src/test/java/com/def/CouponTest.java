package com.def;

import com.def.enums.CouponPublishEnum;
import org.junit.Test;

public class CouponTest {

    @Test
    public void enumTest() {
        m(CouponPublishEnum.PUBLISH.name());
    }

    void m(Object s) {
        System.out.println("PUBLISH".equals(s));
    }

}
