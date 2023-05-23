package com.example.demo.config;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class CognitoSubClaimAdapter implements Converter<Map<String, Object>, Map<String, Object>> {

    private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> convertedClaims = this.delegate.convert(claims);

//        if (claims.containsKey("cognito:groups")) {
//            convertedClaims.put("authorities", claims.get("cognito:groups"));
//        }
//
//        if(claims.containsKey("email")) {
//            convertedClaims.put("email", claims.get("email"));
//        }
//
//        if (claims.containsKey("sub")) {
//            convertedClaims.put("sub", claims.get("sub"));
//        }

        return convertedClaims;
    }
}
