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
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        // Authorization: Bearer <token>
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}