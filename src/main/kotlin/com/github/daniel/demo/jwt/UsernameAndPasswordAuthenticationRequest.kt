package com.github.daniel.demo.jwt

import com.fasterxml.jackson.annotation.JsonProperty


data class UsernameAndPasswordAuthenticationRequest (
        @JsonProperty("username")
        val username: String,

        @JsonProperty("password")
        val password: String
        ) {
}