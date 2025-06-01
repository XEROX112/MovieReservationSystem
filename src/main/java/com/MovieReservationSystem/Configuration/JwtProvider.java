package com.MovieReservationSystem.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Service
public class JwtProvider {

    private SecretKey key= Keys.hmacShaKeyFor((JWTConstant.SECRET_KEY.getBytes()));

    public String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String  roles=populateAuthorities(authorities);
        String token= Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration((new Date(new Date().getTime()+864000)))
                .claim("email", authentication.getName())
                .claim("authorities",roles)
                .signWith(key)
                .compact();
        return token;
    }

    public  String getEmailFromToken(String token) {
        token = token.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String email=String.valueOf(claims.get("email"));
        return email;
    }
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet=new HashSet<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            authoritiesSet.add(grantedAuthority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
