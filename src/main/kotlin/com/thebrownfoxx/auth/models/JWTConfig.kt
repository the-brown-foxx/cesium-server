package com.thebrownfoxx.auth.models

import kotlin.time.Duration

data class JWTConfig(
    val realm: String,
    val issuer: String,
    val audience: String,
    val validity: Duration,
    val secret: String,
)