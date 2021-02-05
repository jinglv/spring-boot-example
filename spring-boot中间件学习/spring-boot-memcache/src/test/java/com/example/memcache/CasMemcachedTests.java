package com.example.memcache;

import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.GetsResponse;
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
public class CasMemcachedTests {
    @Autowired
    private MemcachedClient memcachedClient;

    @Test
    public void testCas() throws Exception {
        memcachedClient.set("cas", 0, 100);
        GetsResponse<Integer> result = memcachedClient.gets("cas");
        System.out.println("result value " + result.getValue());

        long cas = result.getCas();
        // 尝试将a的值更新为200
        if (!memcachedClient.cas("cas", 0, 200, cas)) {
            System.err.println("cas error");
        }
        System.out.println("cas value " + memcachedClient.get("cas"));

        memcachedClient.cas("cas", 0, new CASOperation<Integer>() {
            public int getMaxTries() {
                return 1;
            }

            public Integer getNewValue(long currentCAS, Integer currentValue) {
                return 300;
            }
        });
        System.out.println("cas value " + memcachedClient.get("cas"));

    }

}
