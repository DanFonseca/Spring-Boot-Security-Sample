package com.github.daniel.demo.jwt

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.stream.Collectors
import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenVerifier (
        private val jwtConfig: JwtConfig,
        private val secretKey: SecretKey
        ) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest,
                                  response:  HttpServletResponse,
                                  filterChain: FilterChain) {

        val authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader())

        if(authorizationHeader.isNullOrEmpty() || !authorizationHeader.startsWith(jwtConfig.tokenPrefix)){
            filterChain.doFilter(request, response)
            return
        }

        val token = authorizationHeader.replace(jwtConfig.tokenPrefix,"")

        try{

            val claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)

            val body = claimsJws.body
            val username = claimsJws.signature
            val authorities =  body["authorities"] as  List<Map<String, String>>

            val simpleGrantedAuthorities = authorities.stream()
                    .map { x -> SimpleGrantedAuthority(x["authority"])}
                    .collect(Collectors.toSet())


            val authentication = UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            )

            SecurityContextHolder.getContext().authentication = authentication

        }catch (e : JwtException){
            throw IllegalStateException("Token:$token \n cannot be trust")
        }

        filterChain.doFilter(request, response)

    }
}