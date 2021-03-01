package com.def.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PageUtil {

    public static Map getPage(IPage iPage, Class clazz) {
        Map map = new HashMap(4);
        map.put("current", iPage.getCurrent());
        map.put("size", iPage.getSize());
        map.put("total", iPage.getTotal());
        map.put("records", iPage.getRecords().stream()
                .map(one -> {
                    Object vo = null;
                    try {
                        vo = clazz.newInstance();
                        BeanUtils.copyProperties(one, vo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return vo;})
                .collect(Collectors.toList()));
        return map;
    }

}
