package com.thebrownfoxx.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class ChangeCredentials(
    val oldPassword: String,
    val newPassword: String,
)