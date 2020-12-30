package com.github.daniel.demo.auth

interface ApplicationUserDAO {
   fun  selectApplicationUserByUsername(username: String) : ApplicationUser?
}