package com.thebrownfoxx.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val password: String)