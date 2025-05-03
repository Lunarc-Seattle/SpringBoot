package com.pm.authservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder()
                .decode(secret.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10)) // 10 hours
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        //输入：JWT token 字符串
        //返回：void （但它会通过 抛异常 来表示 token 是否有效）
        //✔️ 如果验证成功 ➔ 什么也不做（不抛异常）
        //❌ 如果验证失败 ➔ 抛 JwtException
        try {
            Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);
            //核心验证逻辑
            //它做了什么？
            //Jwts.parser() ➔ 打包创建一个 JWT 解析器
            //.verifyWith((SecretKey) secretKey) ➔
                //告诉解析器：验证这个 token 的签名，用的 这个 secretKey
                //也就是你的服务端私钥/密钥
            //.build().parseSignedClaims(token) ➔
                //开始真正去解析和验证这个 token
                //它会自动检查：
                //📝 token 格式是否正确
                //🔐 签名是否匹配
                //⏰ 是否过期（如果 token 里有 exp 字段）
            //如果 验证成功 ➔ 返回解析结果
            //如果 失败 ➔ 抛出异常（JwtException 或 SignatureException）
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
            //如果 签名不匹配（比如有人伪造了 token） ➔ 抛一个新的 JwtException，说 "Invalid JWT signature"

        } catch (JwtException e) {
            throw new JwtException("Invalid JWT");
            //如果其他 JWT 验证出错（比如 token 格式错了、过期了、损坏了）
            // ➔ 也抛一个 JwtException，说 "Invalid JWT"
        }
        //结：这个方法逻辑简单明了
        //✔️ 如果 token 有效 ➔ 不抛异常
        //❌ 如果 token 无效（签名错、格式错、过期等） ➔ 抛 JwtException
    }
}