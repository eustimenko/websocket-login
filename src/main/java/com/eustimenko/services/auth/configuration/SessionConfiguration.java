package com.eustimenko.services.auth.configuration;

import org.springframework.context.annotation.*;
import org.springframework.session.*;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.servlet.ServletContext;

@Configuration
public class SessionConfiguration {

    private Integer maxInactiveIntervalInSeconds = 60;

    @Bean
    public MapSessionRepository sessionRepository() {
        final MapSessionRepository sessionRepository = new MapSessionRepository();
        sessionRepository.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        return sessionRepository;
    }

    @Bean
    public SessionRepositoryFilter<ExpiringSession> springSessionRepositoryFilter(MapSessionRepository repository, ServletContext context) {
        final SessionRepositoryFilter<ExpiringSession> filter = new SessionRepositoryFilter<>(repository);
        filter.setServletContext(context);
        return filter;
    }
}
