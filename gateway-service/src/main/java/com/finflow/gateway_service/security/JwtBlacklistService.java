package com.finflow.gateway_service.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class JwtBlacklistService {

    private final StringRedisTemplate redisTemplate ;

    public JwtBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklist(String token , Duration remainingTime)
    {
        if(remainingTime.isZero() || remainingTime.isNegative())
        {
            return ;
        }

        String key = "jwt:blacklist:"+token ;


        redisTemplate.opsForValue().set(
                key,
                "blacklisted",
                remainingTime
        );
    }

    public boolean isBlacklisted(String token)
    {
        String key = "jwt:blacklist:"+token ;
        boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(key));



        return exists ;

    }
}
