package com.demo.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {


    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 生成私钥

    // 外部可能需要用到 token 时间，要设置为 public
    public static final long tokenTime = 30 * 60; // 默认 30 分钟

    /**
     * 生成 token
     * @param sub JWT 面向的用户，一般是 id 或用户名
     * @return
     */
    public static String generateToken(String sub) {
        // 设置用户和超时时间
        Map<String, Object> claims = new HashMap<>(); // payload 部分数据
        claims.put("sub", sub); // 默认属性 subject，可以直接使用 getSubject 获取
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(tokenTime);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        claims.put("exp", Date.from(zonedDateTime.toInstant())); // 超时时间

        // 生成 token 并返回
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

    public static Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }

    public static String parseTokenReturnSubject(String token) throws JwtException {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean checkToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException ex) {
            // token 解析异常
            return false;
        }
    }

    public static void main(String[] args) {

        String token = generateToken("hao");

        String subject = parseTokenReturnSubject(token);

        // Checkperm

        System.out.println(subject);
    }


}
