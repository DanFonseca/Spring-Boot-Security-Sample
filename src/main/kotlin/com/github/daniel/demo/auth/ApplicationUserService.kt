package com.github.daniel.demo.auth

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ApplicationUserService (
        @Qualifier("fake") val applicationUserDAO : ApplicationUserDAO
        ) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
       return  applicationUserDAO
               .selectApplicationUserByUsername(username)
                ?: throw UsernameNotFoundException("User name $username not Found")
    }
}
