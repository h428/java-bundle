package com.hao.bundle.demo.common.component;


import com.hao.bundle.demo.common.props.JwtProperties;
import com.hao.bundle.demo.common.util.RsaUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JWT 工具类
 *
 * @author hao
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * Login Token 有效时间：30分钟，refresh token 时间为 login token 时间的两倍
     */
    public static final long LOGIN_TOKEN_SECOND = 30 * 60;
//    public static final long LOGIN_TOKEN_SECOND = 180 * 24 * 60 * 60; // 半年 token

    private static PrivateKey PRIVATE_KEY;
    private static PublicKey PUBLIC_KEY;

    @Autowired
    private JwtProperties jwtProperties;

    @PostConstruct
    public void init() {
        try {
            // 获取公钥和私钥
            String pubKeyPath = jwtProperties.getPubKeyPath();
            if (StringUtils.isNotBlank(pubKeyPath)) {
                PUBLIC_KEY = RsaUtil.getPublicKey(pubKeyPath);
                log.info("成功获取公钥，地址为 " + pubKeyPath);
            }

            String priKeyPath = jwtProperties.getPriKeyPath();
            if (StringUtils.isNotBlank(priKeyPath)) {
                PRIVATE_KEY = RsaUtil.getPrivateKey(priKeyPath);
                log.info("成功获取私钥，地址为 " + priKeyPath);
            }
        } catch (Exception e) {
            log.error("初始化公钥失败！", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 使用私钥加签，生成 token
     *
     * @param id 主键，标准 payload 属性之一
     * @return token
     */
    public static String generateToken(String id) {
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(LOGIN_TOKEN_SECOND);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());

        return Jwts.builder()
            .setId(id) // 标准的 payload 属性有 id, sub 等，此处只采用 ID
            .setExpiration(date)
            .signWith(PRIVATE_KEY, SignatureAlgorithm.RS256) // 采用非对称加密，私钥加签
            .compact();
    }


    /**
     * 解析 token 获取 id
     *
     * @param token token
     * @return id
     */
    public static String parseTokenReturnSubject(String token) {
        try {

            // claims 实际上就是 payload
            Claims payload = Jwts.parserBuilder()
                .setSigningKey(PUBLIC_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return payload.getId();
        } catch (JwtException e) {
            // 解析失败返回 null
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        String publicKeyPath = "C:\\lab\\key\\lab.pub";
        String privateKeyPath = "C:\\lab\\key\\lab";
        // 读取公钥私钥对
        PublicKey publicKey = RsaUtil.getPublicKey(publicKeyPath);
        PrivateKey privateKey = RsaUtil.getPrivateKey(privateKeyPath);

        JwtUtil.PUBLIC_KEY = publicKey;
        JwtUtil.PRIVATE_KEY = privateKey;

        // 生成 token
        String token = JwtUtil.generateToken("1");
        System.out.println(token);

        // 解析 token
        String id = JwtUtil.parseTokenReturnSubject(token);
        // token 对应的 id 为 1
        System.out.println(id);
    }


}