package com.sequenceiq.cloudbreak.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.sequenceiq.cloudbreak.common.model.user.IdentityUser;
import com.sequenceiq.cloudbreak.service.AuthenticatedUserService;
import com.sequenceiq.cloudbreak.service.RestRequestThreadLocalService;

public class IdentityUserConfiguratorFilter extends OncePerRequestFilter {

    private final RestRequestThreadLocalService restRequestThreadLocalService;

    private final AuthenticatedUserService authenticatedUserService;

    public IdentityUserConfiguratorFilter(RestRequestThreadLocalService restRequestThreadLocalService, AuthenticatedUserService authenticatedUserService) {
        this.restRequestThreadLocalService = restRequestThreadLocalService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        IdentityUser identityUser = authenticatedUserService.getCbUser();
        restRequestThreadLocalService.setIdentityUser(identityUser);
        filterChain.doFilter(request, response);
        restRequestThreadLocalService.removeIdentityUser();
    }
}
