package com.github.daniel.demo.security

import com.github.daniel.demo.security.ApplicationUserPermission.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

enum class ApplicationUserRole (
        private val permissions: Set<ApplicationUserPermission>
        ) {
    ADMIN(hashSetOf(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    ADMIN_TRAINEE(hashSetOf(COURSE_READ,  STUDENT_READ)),
    STUDENT(hashSetOf());

     fun getAuthorities () : Set<SimpleGrantedAuthority> {
        val authorities = permissions.stream()
                .map { x -> SimpleGrantedAuthority(x.permission) }
                .collect(Collectors.toSet())
        authorities.add(SimpleGrantedAuthority("ROLE_$this"))

        return authorities
    }
}