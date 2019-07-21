package com.footmap.user;

import com.footmap.user.utils.footMapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserApplicationTests {

    @Test
    public void contextLoads() {
        String wangyu = footMapUtils.MD5("wangyu" + UUID.randomUUID().toString().substring(0,5));
        System.out.println(wangyu);
    }

}
