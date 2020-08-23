package com.zkytech.authserver.config;

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

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
//    public AuthorizationServer() {
//        super();
//    }

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 配置客户端详情信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // 使用in-memory存储
                .withClient("c1") // client-id
                .secret("secret") // 客户端密钥
                .resourceIds("res1")    // 客户端可以访问的资源列表
                .authorizedGrantTypes("authorization_code", "password",
                        "client_credentials","implicit","refresh_token") // 允许的授权类型
                .scopes("all") // 允许的授权范围
                .autoApprove(true) // false，跳转到授权页面, true，不跳转授权页面，直接发令牌
                .redirectUris(
                        "https://open.bot.tmall.com/oauth/callback");  // 回调地址
    }
    /**
     * 配置令牌（token）的访问端点（endpoint）
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)   // 密码模式需要
                .authorizationCodeServices(authorizationCodeServices)   // 授权码模式需要
                .tokenServices(tokenServices())    // 令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST,HttpMethod.GET); // 允许POST提交
    }


    /**
     * 配置令牌端点（endpoint）的安全策略
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")  // 公开 /oauth/token_key
                .checkTokenAccess("permitAll()")    // 公开 /oauth/check_token
                .allowFormAuthenticationForClients(); // 表单认证 申领令牌
    }



    /**
     * 令牌服务配置
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService); // 客户端信息服务
        service.setSupportRefreshToken(true); // 是否产生刷新令牌
        service.setTokenStore(tokenStore); // 令牌存储策略
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

    //设置授权码模式的授权码如何存取，暂时采用内存方式
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }
}
