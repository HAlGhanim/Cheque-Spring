package com.cornerstone.cheque.auth

import com.cornerstone.cheque.auth.jwt.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val userDetailsService: UserDetailsService
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/auth/register").permitAll()
                    .requestMatchers("/api/users").hasRole("ADMIN")
                    .requestMatchers("/api/users/{id}").hasRole("ADMIN")
                    .requestMatchers("/api/transactions").permitAll()
                    .requestMatchers("/api/auth/**").hasRole("ADMIN")
                    .requestMatchers("/account/{accountNumber}").hasRole("ADMIN")
                    .requestMatchers("/account/getAll").hasRole("ADMIN")
                    .requestMatchers("/api/payment-links/getAll").hasRole("ADMIN")
                    .requestMatchers("/api/kyc/getAll").hasRole("ADMIN")
                    .requestMatchers("/api/invoices/user/{userId}").hasRole("ADMIN")
                    .requestMatchers("/api/invoices/account/{accountNumber}").hasRole("ADMIN")
                    .requestMatchers("/api/invoices/getAll").hasRole("ADMIN")
                    .requestMatchers("/api/accounts/{accountNumber}").hasRole("ADMIN")
                    .requestMatchers("/api/accounts/getAll").hasRole("ADMIN")


                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }
}