package com.github.daniel.demo.security

import com.github.daniel.demo.auth.ApplicationUserService
import com.github.daniel.demo.jwt.JwtConfig
import com.github.daniel.demo.jwt.JwtTokenVerifier
import com.github.daniel.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter
import com.github.daniel.demo.security.ApplicationUserRole.STUDENT
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import javax.crypto.SecretKey

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ApplicationSecurityConfig (

        val passwordEncoder: PasswordEncoder,
        val applicationUserService: ApplicationUserService,
        private val jwtConfig: JwtConfig,
        private val secretKey: SecretKey

) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                    .addFilterAt(JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter::class.java)
                .authorizeRequests()
                .antMatchers("/login", "/", "/index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasAnyRole(STUDENT.name)
                .anyRequest()
                .authenticated()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
            auth?.authenticationProvider(authenticationProvider())
    }

    @Bean
    fun authenticationProvider () : DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(applicationUserService)
        return provider
    }

}