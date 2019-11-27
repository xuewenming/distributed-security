package com.sam.security.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author Mr.xuewenming
 * @title: TokenConfig
 * @projectName distributed-security
 * @description: 令牌配置类
 * @date 2019/11/2720:32
 */
@Configuration
public class TokenConfig {

    @Bean
   public TokenStore tokenStore() {
        // 使用内存存储令牌（普通令牌）
        return new InMemoryTokenStore();
   }


}
