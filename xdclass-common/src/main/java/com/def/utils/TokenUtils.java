package com.def.utils;

import com.def.module.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenUtils {

    private static final String SUBJECT = "wr";

    private static final String SECRET = "tencent_yun";

    private static final int EXPIRE = 1000 * 60 * 60 * 24;

    private static final String TOKEN_PREFIX = "XDCLASS";
    /**
     * 根据用户信息，生成令牌
     *
     * @param tokenInfo
     * @return
     */
    public static String geneJsonWebToken(TokenInfo tokenInfo) {

        Long userId = tokenInfo.getId();
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", userId)
                .claim("name", tokenInfo.getName())
                .claim("mail", tokenInfo.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

        token = TOKEN_PREFIX + token;

        return token;
    }

    /**
     * 校验token的方法
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {

        try {

            final Claims claims = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();

            return claims;

        } catch (Exception e) {
            throw e;
        }

    }


}
