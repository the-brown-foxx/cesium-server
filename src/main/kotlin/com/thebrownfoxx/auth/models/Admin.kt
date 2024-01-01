package com.thebrownfoxx.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class Admin(
    val passwordKey: Long,
    val passwordHash: Hash,
)