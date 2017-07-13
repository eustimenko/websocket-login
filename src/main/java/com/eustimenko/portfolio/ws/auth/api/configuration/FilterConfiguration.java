package com.eustimenko.portfolio.ws.auth.api.configuration;

import com.eustimenko.portfolio.ws.auth.api.filter.AuthFilter;
import org.springframework.context.annotation.*;

import javax.servlet.Filter;

@Configuration
public class FilterConfiguration {

    @Bean
    public Filter authFilter() {
        return new AuthFilter();
    }
}
