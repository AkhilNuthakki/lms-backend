package com.lms.demo.config;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    private static final String INVALID_TOKEN_ERROR = "Invalid Token";

    public String getUsernameFromToken(String token) {
        JWTClaimsSet claimsSet = getAllClaimsFromToken(token);
        if(claimsSet != null){
            return claimsSet.getIssuer();
        }
        return null;

    }

    public Date getExpirationDateFromToken(String token) {
        JWTClaimsSet claimsSet = getAllClaimsFromToken(token);
        if(claimsSet != null) {
            return claimsSet.getExpirationTime();
        }
        return new Date();

    }

    private JWTClaimsSet getAllClaimsFromToken(String token) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            LOG.error(INVALID_TOKEN_ERROR);
            return null;
        }
        try {
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            LOG.error(INVALID_TOKEN_ERROR);
            return null;
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
