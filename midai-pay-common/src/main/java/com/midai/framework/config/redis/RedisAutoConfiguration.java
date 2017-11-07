/**
 * Project Name:midai-pay-common
 * File Name:RedisConfig.java
 * Package Name:com.midai.framework.config.redis
 * Date:2016年9月4日上午10:41:26
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.config.redis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * ClassName:RedisConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月4日 上午10:41:26 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
@EnableCaching
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnNotWebApplication
public class RedisAutoConfiguration {
    
    @Bean
    public RedisTemplate<String, byte[]> redisTemplate(RedisConnectionFactory  redisConnectionFactory){
        RedisTemplate<String, byte[]> redisTemplate =new RedisTemplate<String, byte[]>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new KryoRedisSerializer<Object>());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
    
    @Bean
    public CacheManager cacheManager(RedisTemplate<String, byte[]> redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // Number of seconds before expiration. Defaults to unlimited (0)
        cacheManager.setDefaultExpiration(0);// Sets the default expire time (in seconds)
       
        return cacheManager;
    }
    
    @Bean
    public KeyGenerator customKeyGenerator() {
        return new KeyGenerator() {
            
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
                
            }
        };
    }
    
    @Bean
    public CacheResolver  customCacheResolver(final CacheManager cacheManager){
        
      return  new CacheResolver() {
            
            @Override
            public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
                List<Cache> caches = new ArrayList<Cache>();
                for(String cacheName : context.getOperation().getCacheNames()) {
                  caches.add(cacheManager.getCache(cacheName));
                }
                return caches;
            }
        };
    }
     
    
    

}

