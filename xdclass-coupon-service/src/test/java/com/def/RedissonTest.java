package com.def;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = CouponApplication.class)
@RunWith(SpringRunner.class)
public class RedissonTest {

    @Autowired
    private RedissonClient client;

    @Test
    public void lockTest() throws InterruptedException {
        RLock lock = client.getLock("lock");
        new Thread(()->{
            try {
                lock.tryLock(10, 30, TimeUnit.SECONDS);
                TimeUnit.SECONDS.sleep(10);
//                System.out.println("讲道理我已经释放了锁--------------->");
//                TimeUnit.SECONDS.sleep(30);
                throw new RuntimeException("sdadsa");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();

//        Thread t1 = new Thread(()->{
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                lock.tryLock(10, 5, TimeUnit.SECONDS);
//                System.out.println("我要得到锁------>");
//                TimeUnit.SECONDS.sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                lock.unlock();
//            }
//        });
//        t1.start();
        TimeUnit.SECONDS.sleep(30);
        //t1.join();
    }

}
