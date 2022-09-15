package com.gs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
/**
 * WebMvc自动配置类
 * User: lys
 * DateTime: 2022-04-29
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${fileaddress.logoPath}")
    protected String logoPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String location = "file:" +logoPath + "/";
        registry.addResourceHandler("/game/v1.0/app/gameteam/manager/*/**").addResourceLocations(location);

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //super.addResourceHandlers(registry);

    }

    @Bean
    HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPeriod(true);
        return firewall;
    }
}