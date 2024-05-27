package com.example.bookstore_app.utils;

import com.example.bookstore_app.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    private static final String SECRET_KEY = "my-secret-cubic-top-token-library-plus-plus-key";
//    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000; //60 min
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 60 * 1000; //60 min

    public static String generateAccessToken(int id, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("role", role);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Date expirationDate = getClaimsFromToken(token).getExpiration();
            boolean isTokenExpired = expirationDate.before(new Date());
            return !isTokenExpired;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

}
