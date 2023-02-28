package com.lms.demo.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
public class JWTAuthTokenConfiguration implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 1000L;
    @Value("${jwt.secret}")
    private String secret;

    @SneakyThrows
    public SignedJWT generateToken(String userRole) {

        RSAKey rsaJWK = new RSAKeyGenerator(2048)
                .keyID(secret)
                .generate();

        JWSSigner signer = new RSASSASigner(rsaJWK);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("lms")
                .issuer("lms-signup")
                .audience("lms-backend")
                .expirationTime(new Date(new Date().getTime() + JWT_TOKEN_VALIDITY))
                .claim("role", userRole)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(), claimsSet);

        signedJWT.sign(signer);



        return signedJWT;

    }


}

