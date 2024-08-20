package com.iksling.blog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static String key;

    @Value("${jwt.key}")
    public void setKey(String key) {
        JwtUtil.key = key;
    }

    public static String createJwtToken(String id, String subject) {
        JwtBuilder builder = getJwtBuilder(id, subject);
        return builder.compact();
    }

    public static Claims parseJwtToken(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private static JwtBuilder getJwtBuilder(String id, String subject) {
        return Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
    }
}
