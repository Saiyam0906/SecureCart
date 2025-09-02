package com.example.SecureCart.Security;


import java.security.PrivateKey;

import java.security.PublicKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {
	
	private static final String TOKEN_TYPE="token_type";
	private static final String ACCESS_TOKEN_TYPE = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH_TOKEN";
    
    
	private final PrivateKey privatekey;
	private final PublicKey publickey;
	
	@Value("${security.jwt.access-token-expiration}")
	private long accessTokenExpiration;
	
	@Value("${security.jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;
	
	public JwtService(){
		try {
			this.privatekey = KeyUtils.loadPrivateKey("keys/local-only/private.pem");
	        this.publickey  = KeyUtils.loadPublicKey("keys/local-only/public.pem");
			log.info("JWT keys loaded successfully");
		}catch (Exception e) {
			log.error("Failed to load JWT keys", e);
			throw new RuntimeException("Could not initialize JWT service", e);
		}	
	}
	
	public String generateAccessToken(final String username) {
		final Map<String,Object> claims= Map.of(TOKEN_TYPE,ACCESS_TOKEN_TYPE);
		return buildToken(username,claims,this.accessTokenExpiration);
				
	}
	
	public String generateRefreshToken(final String username) {
		final Map<String,Object> claims= Map.of(TOKEN_TYPE,REFRESH_TOKEN_TYPE);
		return buildToken(username,claims,this.refreshTokenExpiration);
	}

	private String buildToken(final String username,final Map<String, Object> claims,final long Expirations) {
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + Expirations))
				.signWith(this.privatekey)
				.compact();
	}
	
	public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                .verifyWith(this.publickey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT token is malformed: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("JWT signature validation failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid: {}", e.getMessage());
        }
        return false;
    }
	
	private Claims extractClaims(String token){
		try {
		return Jwts.parser()
				.verifyWith(this.publickey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		}catch (JwtException e) {
			throw new RuntimeException("cant extract claims");
		}
		
	}
	
	public String extractUsername(String token) {	
		if (!validateToken(token)) {
	        return null;  
	    }
		return extractClaims(token).getSubject();
	}
	
	public boolean isTokenExpired(String token) {
		 if (!validateToken(token)) {
		        return true;  
		    }
		return extractClaims(token).getExpiration().before(new Date());
	}

	
	public String extractTokenType(final String token) {
		if (!validateToken(token)) {
	        return null;  
	    }
		final Claims claim=extractClaims(token);
		return claim.get(TOKEN_TYPE,String.class);
	}
	
	public boolean isAccessToken(String token) {
		return ACCESS_TOKEN_TYPE.equals(extractTokenType(token));
	}
	
	public Date extractExpiration(final String token) {
		if (!validateToken(token)) {
	        return null;  
	    }
		return extractClaims(token).getExpiration();
	}
	
	public boolean isRefreshToken(String token) {
		return REFRESH_TOKEN_TYPE.equals(extractTokenType(token));
	}
	
	public boolean validateAccessToken(final String token) {
        return validateToken(token) && isAccessToken(token);
    }

    public boolean validateRefreshToken(final String token) {
        return validateToken(token) && isRefreshToken(token);
    }
    
    public long getTokenRemainingTime(final String token) {
    	if (!validateToken(token)) {
	        return 0;  
	    }
        final Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis();
    }
    
    public String refreshAccessToken(final String refreshToken) {
    	if(!validateRefreshToken(refreshToken)) {
    		throw new RuntimeException("Invalid token type");
    	}
    	final Claims claim=extractClaims(refreshToken);
    	final String username=claim.getSubject();
    	return generateAccessToken(username);
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String usernameFromToken = extractUsername(token); 
        final String usernameFromUserDetails = userDetails.getUsername(); 
        return usernameFromToken.equals(usernameFromUserDetails) && !isTokenExpired(token);
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
