package com.example.memcache.config;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author jingLv
 * @date 2021/02/05
 */
@Configuration
public class MemcachedBuilder {

    protected static Logger logger = LoggerFactory.getLogger(MemcachedBuilder.class);

    @Resource
    private XmemcachedProperties xmemcachedProperties;

    @Bean
    public MemcachedClient getMemcachedClient() {
        MemcachedClient memcachedClient = null;
        try {
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(xmemcachedProperties.getServers()));
            builder.setConnectionPoolSize(xmemcachedProperties.getPoolSize());
            builder.setOpTimeout(xmemcachedProperties.getOpTimeout());
            memcachedClient = builder.build();
        } catch (IOException e) {
            logger.error("inint MemcachedClient failed ", e);
        }
        return memcachedClient;
    }
}
