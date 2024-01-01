package com.thebrownfoxx.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class ChangeCredentials(
    val oldPassword: String,
    val newPassword: String,
)