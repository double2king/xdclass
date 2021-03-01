package com.def;

import com.def.util.CommonUtil;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class NotifyControllerTest {

    @Test
    public void testTimeDiff() {
        String email = "wwgshctftgehbgbb";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        long between = ChronoUnit.SECONDS.between(now, tomorrow);
        LocalDateTime six = LocalDateTime.of(2021, 2, 23, 18, 0, 0);
        LocalDateTime seven = LocalDateTime.of(2021, 2, 23, 19, 0, 0);
        between = Duration.between(six, seven).toMillis();
        between = Duration.between(six, tomorrow).toMillis();
        System.out.println(between);
    }

    @Test
    public void encryption() {
        String select = "$1$" + CommonUtil.getStringNumRandom(8);
        //密码 + 加盐处理
        String cryptPwd = Md5Crypt.md5Crypt("123456".getBytes(), select);
        System.out.println(select);
        System.out.println(cryptPwd);
    }

    @Test
    public void encryption2() {
        String select = "$1$XreCF2aI";
        //密码 + 加盐处理
        String cryptPwd = Md5Crypt.md5Crypt("123456".getBytes(), select);
        System.out.println(cryptPwd);
    }

}
