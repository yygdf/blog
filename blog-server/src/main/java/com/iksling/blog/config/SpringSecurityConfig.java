package com.iksling.blog.config;

import com.iksling.blog.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailHandlerImpl authenticationFailHandler;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    @Autowired
    private InvalidSessionStrategyImpl invalidSessionStrategy;
    @Autowired
    private SessionInformationExpiredStrategyImpl sessionInformationExpiredStrategy;

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new FilterInvocationSecurityMetadataSourceImpl();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AccessDecisionManagerImpl();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginProcessingUrl("/login").successHandler(authenticationSuccessHandler).failureHandler(authenticationFailHandler).and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setSecurityMetadataSource(securityMetadataSource());
                        fsi.setAccessDecisionManager(accessDecisionManager());
                        return fsi;
                    }
                })
                .anyRequest().permitAll()
//                .and()
//                .rememberMe()
//                .key("https://iksling.com")
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .sessionRegistry(sessionRegistry());
    }
}
