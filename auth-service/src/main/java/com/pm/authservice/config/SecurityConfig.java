package com.pm.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//| 功能          | 配置效果         |
//| ----------- | ------------ |
//| 🔓 API接口    | 全部放行，不拦截     |
//| 🛡️ CSRF 防护 | 已关闭（适合 REST） |
//| 🔐 密码加密算法   | 使用 BCrypt    |
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
    //任何请求 (anyRequest()) ➔ 都允许访问（permitAll()）。
    //关闭 CSRF 防护（.csrf().disable()）：
    //🚩 CSRF 是表单提交用的防护。
    //你是 REST API 项目 ➔ 这个通常需要关闭（不然接口 POST/PUT 会 403）。
    //
    //现实例子：
    //请求	是否放行？
    //POST /login	✅ 放行
    //GET /patients	✅ 放行
    //DELETE /users	✅ 放行
    //
    //👉 所有请求，无条件放行。

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //项目里所有用户密码都会用 BCrypt 算法加密。
    //✅ 安全、通用，很多大公司（Google、GitHub）也用。
    //String rawPassword = "123456";
    //String encoded = passwordEncoder.encode(rawPassword);
    //encoded 可能变成: $2a$10$as9v8uA...
}
    //以后项目升级可以这样改）
    //如果要 保护 某些接口（比如 /admin/**），可以改成：
    //http.authorizeHttpRequests(authorize ->
    //    authorize
    //      .requestMatchers("/admin/**").authenticated()
    //      .anyRequest().permitAll()
    //);
    //这样：只有登录成功的人才能访问 /admin/**。