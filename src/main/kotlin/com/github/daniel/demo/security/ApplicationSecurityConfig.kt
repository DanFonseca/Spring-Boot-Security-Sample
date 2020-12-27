package com.github.daniel.demo.security

import com.github.daniel.demo.security.ApplicationUserPermission.*
import com.github.daniel.demo.security.ApplicationUserRole.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
@EnableWebSecurity
class ApplicationSecurityConfig (
        val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasAnyRole(STUDENT.name)
                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.name)
                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.name)
                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.name)
                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name, ADMIN_TRAINEE.name)
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {

        val  daniel : UserDetails = User.builder()
                .username("danfreitas")
                .password(passwordEncoder.encode("123"))
               // .roles(STUDENT.name)
                .authorities(STUDENT.getAuthorities())
                .build()

        val lindaUser = User.builder()
                .username("Linda")
                .password(passwordEncoder.encode("123"))
               // .roles(ADMIN.name)
                .authorities(ADMIN.getAuthorities())
                .build()


        val tom = User.builder().
                username("tom")
                .password("123")
               // .roles(ADMINTRAINEE.name)
                .authorities(ADMIN_TRAINEE.getAuthorities())
                .build()

        return InMemoryUserDetailsManager(
                daniel,
                lindaUser,
                 tom)
    }
}