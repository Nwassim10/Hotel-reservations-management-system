package edu.miu.cs.cs544.config;


import edu.miu.cs.cs544.service.UserCheck;
import edu.miu.cs.cs544.service.impl.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@Profile("prod")
public class WebSecurityConfig {

    @Autowired
    private UserCheck userService;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    private static final String[] WHITE_LIST_URLS = {
            "/api/register-or-login",
            "/register*",
            "/registrationConfirm",
            "/savePassword",
            "/registrationConfirm",
            "/savePassword",
            "/login*",
            "/resetPassword*",
            "/resendRegistrationToken",
            "/logout*", "/error*","/oauth2/**","/login/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(WHITE_LIST_URLS).permitAll()
                                .requestMatchers("/api/**").authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .defaultSuccessUrl("/", true)
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("/", true)
                )
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                );


        return http.build();
    }
   @Autowired
   private PasswordEncoder passwordEncoder;

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://accounts.google.com"); // Replace with your issuer URI
    }
}
