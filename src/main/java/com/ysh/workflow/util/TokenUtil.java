package com.ysh.workflow.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class TokenUtil {
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000 * 30L;
    private static final String TOKEN_SECRET = "workflowtoken";

    public static String createToken(int userId, String username, String password) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withExpiresAt(date).sign(algorithm);
    }

    public static boolean verifyToken(String token) {
        if (token == null) {
            return false;
        }
        JWTVerifier verifer = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
        try {
            verifer.verify(token);
        } catch (JWTVerificationException e) {
            log.info("token无法验证:" + e);
            return false;
        }
        return true;
    }
}
