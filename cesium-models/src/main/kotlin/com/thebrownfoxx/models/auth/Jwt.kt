package com.thebrownfoxx.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class Jwt(val value: String)