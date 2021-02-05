package com.example.memcache;

import net.rubyeye.xmemcached.MemcachedClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author jingLv
 * @date 2021/02/05
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TouchMemcachedTests {

    @Autowired
    private MemcachedClient memcachedClient;

    @Test
    public void testTouch() throws Exception {
        memcachedClient.set("Touch", 2, "Touch Value");
        Thread.sleep(1000);
        memcachedClient.touch("Touch", 6);
        Thread.sleep(2000);
        String value = memcachedClient.get("Touch", 3000);
        System.out.println("Touch=" + value);
    }
}
