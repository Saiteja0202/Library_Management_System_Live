package com.cts.library.configuration;

import com.cts.library.filter.AuthenticationFilter;
import com.cts.library.authentication.CurrentUser;
import com.cts.library.repository.MemberTokenRepo;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(MemberTokenRepo repo, CurrentUser currentUser) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter(repo, currentUser));
        registrationBean.setOrder(2); 
        return registrationBean;
    }
}
