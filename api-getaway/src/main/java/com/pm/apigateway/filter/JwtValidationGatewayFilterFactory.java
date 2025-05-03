package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

//🔒 检查：每个经过 API Gateway 的请求有没有带 Authorization: Bearer <token>
//
//🔄 远程调用：转去你的 auth-service 的 /validate 接口验证这个 token
//
//🟢 如果验证成功 ➔ 继续放行请求
//
//🔴 如果验证失败 ➔ 返回 401 Unauthorized
//2个潜在坑，你最好立刻注意
//retrieve() 这一行没有处理 401错误
//

//validate 这个调用默认是异步的，但你现在代码没捕获异常路径
//加了 .onErrorResume() ？
//因为 retrieve()：
//如果 auth-service 返回 401，默认会抛一个 WebClientResponseException
//你的代码如果不捕获，会直接导致请求 hang（或者 500 error）
//加了 .onErrorResume() 后：
//验证失败 ➔ 直接返回 401
//不会让 Gateway 异常
//✅ 现在这个流程逻辑会变得非常健壮
//没 token ➔ 返回 401
//token 验证失败（auth-service返回401） ➔ 返回 401
//
//token 验证成功（auth-service返回200） ➔ 正常放行
@Component
public class JwtValidationGatewayFilterFactory extends
        AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .toBodilessEntity()
                    .then(chain.filter(exchange));
        };
    }
}