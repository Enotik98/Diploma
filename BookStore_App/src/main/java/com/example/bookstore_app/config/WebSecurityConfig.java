package com.example.bookstore_app.config;

import com.example.bookstore_app.filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure (HttpSecurity http) throws Exception{
        http.csrf().disable()
                    .cors().configurationSource(corsConfigurationSource())
                .and()
                    .authorizeRequests()
//                        .antMatchers("/user/*").permitAll()
                        .antMatchers("/api/login").permitAll()
                        .antMatchers("/api/registration").permitAll()
                        .antMatchers("/api/token").permitAll()
                        .antMatchers("/api/book").permitAll()
                        .antMatchers("/api/book/{id}").permitAll()
                        .antMatchers("/api/book/genre").permitAll()
//                        .antMatchers("/order/*").permitAll()
//                        .antMatchers("/extension-request/*").permitAll()
//                        .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .addFilterBefore(new TokenFilter(), UsernamePasswordAuthenticationFilter.class);


    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Дозволяємо доступ з будь-якого джерела
        configuration.addAllowedMethod("*"); // Дозволяємо використання будь-яких HTTP-методів
        configuration.addAllowedHeader("*"); // Дозволяємо використання будь-яких заголовків

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
