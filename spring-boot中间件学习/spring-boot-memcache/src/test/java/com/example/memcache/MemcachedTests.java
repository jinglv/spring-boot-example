package com.example.memcache;

import net.rubyeye.xmemcached.Counter;
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
public class MemcachedTests {
    @Autowired
    private MemcachedClient memcachedClient;

    /**
     * 测试Memcached的Incr 和 Decr
     *
     * @throws Exception
     */
    @Test
    public void testCounter() throws Exception {
        Counter counter = memcachedClient.getCounter("counter1", 10);
        System.out.println("counter=" + counter.get());
        long c1 = counter.incrementAndGet();
        System.out.println("counter=" + c1);
        long c2 = counter.decrementAndGet();
        System.out.println("counter=" + c2);
        long c3 = counter.addAndGet(-10);
        System.out.println("counter=" + c3);
    }
}
