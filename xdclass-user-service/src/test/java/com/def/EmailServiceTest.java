package com.def;

import com.UserApplication;
import com.def.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    public void testSendEmail() {
        mailService.sendEmail("2214680608@qq.com", "这是主题", "这是内容");
    }

}
