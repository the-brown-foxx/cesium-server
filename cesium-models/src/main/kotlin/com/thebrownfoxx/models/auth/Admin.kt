package com.thebrownfoxx.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class Admin(
    val passwordKey: Long,
    val passwordHash: Hash,
)