package com.example.demo.filter;

import com.example.demo.config.FakeUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {

    private static final List<String> SKIP_PATHS = Arrays.asList("/docs");

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tenantAppUserId = "", scopes = "", tenantUserId = "";

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuthenticationToken.getToken();

            Map<String, Object> convertedClaims = jwt.getClaims();

            tenantUserId = convertedClaims.get("sub").toString();
            tenantAppUserId = convertedClaims.get("tenant_app_user_id").toString();
            scopes = convertedClaims.get("scopes").toString();
            addSpringSecurityTokenHelper(Optional.of(FakeUserDetail.builder().tenantUserId(tenantUserId).tenantAppUserId(tenantAppUserId).scopes(scopes).build()), httpServletRequest);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void addSpringSecurityTokenHelper(Optional<UserDetails> authUserDetailOptional, HttpServletRequest httpServletRequest) {
        if (!authUserDetailOptional.isPresent()) {
            return;
        }

        UserDetails authUserDetail = authUserDetailOptional.get();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authUserDetail,
                null,
                authUserDetail.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //Set userId and authorities for auditing purpose
//        httpServletRequest.setAttribute(Constant.USER_ID_KEY, String.valueOf(authUserDetail.getId()));
//        httpServletRequest.setAttribute(Constant.USER_AUTHORITY_KEY, authUserDetail.getAuthorities().toString());
//        httpServletRequest.setAttribute(Constant.IMPERSONATED_BY, authUserDetail.getImpersonatedBy());
    }

    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return SKIP_PATHS.stream().anyMatch((p) -> p.equals(path));
    }
}
