package com.thebrownfoxx.models.auth

data class JWTClaim(
    val key: String,
    val value: String,
)
