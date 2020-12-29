package com.github.daniel.demo.security

import com.github.daniel.demo.security.ApplicationUserRole.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.concurrent.TimeUnit

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ApplicationSecurityConfig (
        val passwordEncoder: PasswordEncoder
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/", "/index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasAnyRole(STUDENT.name)
//                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.permission)
//                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.permission)
//                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.permission)
//                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name, ADMIN_TRAINEE.name)
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/courses")
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(TimeUnit.DAYS.toSeconds(21).toInt())
                    .key("somethingverysecured")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher( AntPathRequestMatcher("/logout",  "POST"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","remember-me")
                    .logoutSuccessUrl("/login")
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
                .password(passwordEncoder.encode("123"))
               // .roles(ADMINTRAINEE.name)
                .authorities(ADMIN_TRAINEE.getAuthorities())
                .build()

        return InMemoryUserDetailsManager(
                daniel,
                lindaUser,
                 tom)
    }
}