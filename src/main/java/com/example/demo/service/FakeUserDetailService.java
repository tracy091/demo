package com.example.demo.service;


import com.example.demo.config.FakeUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FakeUserDetailService {
    public String getCurrentId() {
        FakeUserDetail userDetail = (FakeUserDetail) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetail.getTenantUserId();
    }

    public String getScopes() {
        FakeUserDetail userDetail = (FakeUserDetail) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetail.getScopes();
    }

}
