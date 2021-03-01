package com.def;

import com.UserApplication;
import com.def.service.AddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class AddressControllerTest {

    @Autowired
    AddressService addressService;

    @Test
    public void m1() {
        System.out.println(addressService+"----------->");
    }

}
