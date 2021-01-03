package com.github.daniel.demo.jwt

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.net.HttpHeaders
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
class JwtConfig ()
{
    @JsonProperty("secretKey")
    var secretKey: String = ""
    @JsonProperty("tokenPrefix")
    var tokenPrefix: String = ""
    @JsonProperty("tokenExpirationAfterDays")
    var tokenExpirationAfterDays: Long = 0


    fun getAuthorizationHeader(): String {
        return HttpHeaders.AUTHORIZATION
    }
}