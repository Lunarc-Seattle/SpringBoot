package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userService.findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(),
                        u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
        // turn xx into token

        return token;
    }

    public boolean validateToken(String token) {
        //这是一个 验证 token 的方法。
        //输入：一个字符串 token
        //输出：true（有效） 或 false（无效）
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e){
            return false;
        }
        //jwtUtil.validateToken(token);
        // 这是你项目里负责处理 JWT（JSON Web Token）的工具类。
        //它会对这个 token 做一系列验证：是否签名正确，是否过期，是否结构完整
        // 如果 token 没问题，它就什么也不抛，继续往下走。
        //return true;
        //
        //如果验证通过，就返回 true（说明 token 有效）。
        //
        //catch (JwtException e)
        //如果 jwtUtil.validateToken() 抛出了异常（JwtException）
        //说明：token 无效
        //可能是伪造的、过期的、签名错了、结构损坏了
        //return false;
        //捕获到异常 ➔ 返回 false（说明 token 无效）
    }
}