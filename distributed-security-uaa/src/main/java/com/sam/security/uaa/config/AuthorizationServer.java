package com.sam.security.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Mr.xuewenming
 * @title: AuthorizationServer
 * @projectName distributed-security
 * @description: 授权服务配置
 * @date 2019/11/2720:08
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthorizationServerTokenServices tokenService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    /**
     *1. 配置客户端详情 - 方式1 - 内存实现方式
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("c1") // 客户端ID
                .secret(new BCryptPasswordEncoder().encode("secret")) // 客户端秘钥
                .resourceIds("res1")// 资源列表
                // 该Client允许的授权类型
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                .scopes("all") // 允许的授权范围
                .autoApprove(false) // false是跳转到授权页面
                .redirectUris("http://www.baidu.com"); // 回调验证
    }

    /**
     * 2.令牌访问端点配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager) // 1.认证管理器
                .tokenServices(tokenService) // 2.令牌服务
                .authorizationCodeServices(authorizationCodeServices) // 3.授权模式
                .allowedTokenEndpointRequestMethods(HttpMethod.POST); // 4.请求方式
    }

    /**
     * 3.令牌访问端点 - 安全服务策略
     *    /oauth/check_token：用于资源服务访问的令牌解析端点。
     *    /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()") // 1.oauth/token_key是公开的
                .checkTokenAccess("permitAll()") // 2.oauth/check_token是公开的
                .allowFormAuthenticationForClients(); // 3.表单认证（申请令牌）
    }

    /**
     * 令牌服务
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService); // 客户端详情
        tokenServices.setTokenStore(tokenStore); // 令牌存储策略
        tokenServices.setSupportRefreshToken(true); // 支持刷新
        tokenServices.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        tokenServices.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return tokenServices;
    }

    /**
     * 授权码模式的授权码如何存取，暂时采用内存方式
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }
}
