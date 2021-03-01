package com.def.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class CommonUtil {

    public static void returnJsonData(HttpServletResponse response, Object obj) {

        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            ObjectMapper objMapper = new ObjectMapper();
            String json = objMapper.writeValueAsString(obj);
            writer.print(json);
        } catch (Exception e) {
            log.info("token校验失败，转化json时报错，错误为{}", e);
        }

    }

}
