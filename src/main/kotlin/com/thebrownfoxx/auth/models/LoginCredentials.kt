package com.thebrownfoxx.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(val password: String)