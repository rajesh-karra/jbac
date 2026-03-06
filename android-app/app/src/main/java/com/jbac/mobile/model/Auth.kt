package com.jbac.mobile.model

data class LoginRequest(
    val username: String,
    val password: String,
)

data class LoginResponse(
    val token: String,
    val user: User,
)

data class User(
    val username: String,
    val name: String,
    val role: String,
)
