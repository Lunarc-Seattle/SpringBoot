package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.LoginResponseDTO;

import com.pm.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO) {

        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));
        // Spring Boot 的精妙点👇
        //return ResponseEntity.ok(new LoginResponseDTO(token));
        //✔️ **这个行代码确实是妙的**，它：**自动**：
        //  * 创建了一个 HTTP 200 OK 响应
        //  * 把 `LoginResponseDTO` 这个 Java 对象 ➔ 自动**变成 JSON** 返回给前端
        //* ✔️ **不用自己手动 new HTTP Response**，也不用手动序列化成 JSON
        //* ✔️ Spring Boot + Jackson（自动序列化）帮你搞定了
        //### ✅ **为什么不用自己 new 一个 HTTP 响应？**
        //因为 Spring Boot 提供了**非常舒服的封装**：
        //| 传统写法（麻烦）                           | Spring Boot 写法（舒服）             |
        //| ---------------------------------- | ------------------------------ |
        //| 手动创建 Response + 设置 Status + 写 JSON | ➔ `ResponseEntity.ok(对象)` 自动搞定 |
        //| 手动序列化对象 ➔ JSON                     | ➔ 自动用 Jackson 把 DTO ➔ JSON     |
        //### ✅ **为什么能这么写？**
        //因为：* `@RestController` ➔ 自动帮你**把返回的 Java 对象** ➔ **变成 HTTP 响应**
        //* `ResponseEntity.ok(...)` ➔ 自动设置**状态码200** + **返回体**
        //* `LoginResponseDTO(token)` ➔ 这个**构造函数**实际上还是在用


        //  * `new LoginResponseDTO(token)` ➔ 先 new 出 Java 对象
        //  * Spring Boot ➔ 自动把这个**对象序列化成 JSON**

        //### 🔥 **背后发生的事**
        //return ResponseEntity.ok(new LoginResponseDTO(token));
        //⬇️ 实际等于：
        //* `HTTP 200 OK`
        //* `Content-Type: application/json`
        //* 响应体：
        //  "token": "abc.def.ghi"
        //**全部自动搞定**！

        //### ✅ **一句话总结**
        //> ✔️ `ResponseEntity.ok(new DTO)` = **返回 HTTP 200 + 自动 JSON 响应**
        //> ✔️ **构造函数**（`new LoginResponseDTO(token)`）还是在用的
        //> ✔️ Spring Boot 自动帮你把 Java 对象 ➔ JSON（靠 Jackson 库）

        //🌱（这样你会更直观地理解 Spring Boot 的「自动魔法」）

    }

    @Operation(summary = "Validate Token")
    //这是 Swagger/OpenAPI 的注解，用来描述这个接口。
    //当你生成 API 文档时，这个接口会显示为 "Validate Token"。
    @GetMapping("/validate")
    //说明这是一个 GET 请求。路径是 /validate。
    //
    //也就是说，当你请求：GET /validate
    //它就会触发这个方法。
    public ResponseEntity<Void> validateToken(
            //这是这个接口的方法，ResponseEntity<Void> 意味着返回一个 HTTP 响应
            //
            //不带 body（Void 就是空）
            //只用 HTTP 状态码来表示成功/失败。
            @RequestHeader("Authorization") String authHeader) {
            //这个是从 请求头 中拿到 Authorization 字段。
            //例如，前端请求的时候，头部会这样写：
            //Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
            //它会把整个 Authorization 字符串传进来。

        // Authorization: Bearer <token>
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            //如果 Authorization 头：没有传（null）或者不是以 "Bearer " 开头
            //就返回 401 Unauthorized，表示未授权。
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //authHeader.substring(7)：
        //把 "Bearer " 这7个字去掉，拿到真正的token。
        //例如："Bearer abcdefg" ➔ 拿到 "abcdefg"
        //然后调用你的 authService.validateToken(token) 去检查这个 token 是否有效。
        //如果有效（返回 true）：
        //返回 200 OK
        //如果无效（返回 false）：
        //返回 401 Unauthorized
    }
}