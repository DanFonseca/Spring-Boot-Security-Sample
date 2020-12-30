package com.github.daniel.demo.auth

import com.github.daniel.demo.security.ApplicationUserRole.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository("fake")
class FakeApplicationUserService (

        val passwordEncoder: PasswordEncoder

        ) : ApplicationUserDAO {

    override fun selectApplicationUserByUsername(username: String): ApplicationUser? {
        return  getApplicationUsername(username).stream()
                .filter{x -> x?.username.equals(username)}
                .findFirst().get()
    }

    fun getApplicationUsername (username: String) : List<ApplicationUser?> {
        return mutableListOf<ApplicationUser?>(
                ApplicationUser(STUDENT.getAuthorities(),
                        passwordEncoder.encode("123"), "danfreitas",
                        true, true,
                        true, true),

                ApplicationUser(ADMIN.getAuthorities(),
                        passwordEncoder.encode("123"), "linda",
                        true, true,
                        true, true),

                ApplicationUser(ADMIN_TRAINEE.getAuthorities(),
                        passwordEncoder.encode("123"), "tom",
                        true, true,
                        true, true)

        )
    }
}