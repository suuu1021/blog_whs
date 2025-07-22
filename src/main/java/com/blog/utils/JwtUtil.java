package com.blog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blog.user.SessionUser;
import com.blog.user.User;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "whsblog";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private static final String SUBJECT = "blog_whs";

    public static String create(User user) {
        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        String jwt = JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(expiresAt)
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return jwt;
    }

    public static SessionUser verify(String jwt) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .withSubject(SUBJECT)
                .build().verify(jwt);

        Long id = decodedJWT.getClaim("id").asLong();
        String username = decodedJWT.getClaim("username").asString();
        String email = decodedJWT.getClaim("email").asString();
        return SessionUser.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
    }

    public static Long getUserId(String jwt) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .withSubject(SUBJECT)
                .build().verify(jwt);
        return decodedJWT.getClaim("id").asLong();
    }

    public static boolean isValid(String jwt) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .withSubject(SUBJECT)
                    .build().verify(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
