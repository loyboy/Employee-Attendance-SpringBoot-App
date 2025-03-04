package com.example.empattendance.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  /**
   * Do filter internal.
   *
   * @param request The HTTP servlet request
   * @param response The HTTP servlet response
   * @param chain The filter chain
   * @throws ServletException If an error occurs
   * @throws IOException If an error occurs
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    if(authorizationHeader!=null) {
			String jwt= authorizationHeader.substring(7);
			
			try {
				SecretKey key = JwtTokenUtil.secretKey ;
				JwtParser jwtParser = Jwts.parser().verifyWith(key).build();
        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(jwt);
        Claims claims = claimsJws.getPayload();

				String username=String.valueOf(claims.get("username"));
				String authorities=String.valueOf(claims.get("authorities"));
				
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				
				Authentication authentication=new UsernamePasswordAuthenticationToken(username,null, auths);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			} catch (Exception e) {
        System.out.println("error in jwt: " + e.getMessage());
        e.printStackTrace();
			}
		}

    chain.doFilter(request, response);
    
  }
}
