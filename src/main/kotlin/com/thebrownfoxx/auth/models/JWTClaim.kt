package com.thebrownfoxx.auth.models

data class JWTClaim(
    val key: String,
    val value: String,
)
