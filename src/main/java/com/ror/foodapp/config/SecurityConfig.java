package com.ror.foodapp.config;

import com.ror.foodapp.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/","/order-form","/orders/new", "/payment/submit","/payment/confirmation","/register", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/dishes", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Permite el acceso anónimo a las rutas especificadas
                        .requestMatchers("/", "/order-form", "/orders/new", "/payment/submit", "/payment/confirmation", "/payment/cancel",
                                "/order-confirmation","/payment/create-checkout-session","/register", "/login").permitAll()
                        // Protege todas las demás rutas
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // Define la página de inicio de sesión personalizada
                        .defaultSuccessUrl("/dishes", true)  // Redirige a esta página después de un inicio de sesión exitoso
                        .failureUrl("/login?error=true")  // Redirige aquí en caso de fallo
                        .permitAll()  // Permite el acceso a la página de inicio de sesión sin autenticación
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Define la URL para cerrar sesión
                        .logoutSuccessUrl("/login?logout=true")  // Redirige después de cerrar sesión exitosamente
                        .invalidateHttpSession(true)  // Invalida la sesión
                        .deleteCookies("JSESSIONID")  // Elimina las cookies de sesión
                        .permitAll()  // Permite el acceso al cierre de sesión sin autenticación
                );


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

