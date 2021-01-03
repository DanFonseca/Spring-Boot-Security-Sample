package com.github.daniel.demo.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.sql.Date
import java.time.LocalDate
import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtUsernameAndPasswordAuthenticationFilter (

        private val authenticationManage: AuthenticationManager,
        private val jwtConfig: JwtConfig,
        private val secretKey: SecretKey

        ) : UsernamePasswordAuthenticationFilter(){

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        try {
            val  authenticationRequest = ObjectMapper()
                    .readValue(request?.inputStream, UsernameAndPasswordAuthenticationRequest::class.java)

            val authentication = UsernamePasswordAuthenticationToken(
                        authenticationRequest.username,
                        authenticationRequest.password)

            return authenticationManage.authenticate(authentication)
        }catch (e:IOException){
            throw RuntimeException(e)
        }

    }

    override fun successfulAuthentication(request: HttpServletRequest?,
                                          response: HttpServletResponse?,
                                          chain: FilterChain?,
                                          authResult: Authentication?) {

        val token =
                Jwts.builder()
                        .setSubject(authResult?.name)
                        .claim("authorities", authResult?.authorities)
                        .setIssuedAt (java.util.Date())
                        .setExpiration(Date.valueOf(LocalDate.now().plusDays(jwtConfig.tokenExpirationAfterDays)))
                        .signWith(secretKey)
                        .compact()

        response?.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.tokenPrefix + token)

    }

}