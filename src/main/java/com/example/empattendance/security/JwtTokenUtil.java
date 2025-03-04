package com.example.empattendance.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil {

  static SecretKey secretKey = Jwts.SIG.HS256.key().build();

  public static String generateToken(Authentication auth) {
		
		Collection<?extends GrantedAuthority> authorities = auth.getAuthorities();
		String roles = populateAuthorities(authorities);

        return Jwts.builder()
        .claim("username", auth.getName())
				.claim("authorities", roles)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 1 week validity
        .signWith(secretKey)
        .compact();
		
	}
  public static String getEmailFromJwtToken(String jwt) {
		
		jwt=jwt.substring(7);
		JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
    Jws<Claims> claimsJws = jwtParser.parseSignedClaims(jwt);
		String username=String.valueOf(claimsJws.getPayload().get("username"));
		return username;
	}
	
	public static String populateAuthorities(
			Collection<?extends GrantedAuthority> collection)
	{
		Set<String> auths = new HashSet<>();
		
		for(GrantedAuthority authority:collection) {
			auths.add(authority.getAuthority());
		}
     //		"customer,admin,super-admin"
		return String.join(",", auths);
	}
}
