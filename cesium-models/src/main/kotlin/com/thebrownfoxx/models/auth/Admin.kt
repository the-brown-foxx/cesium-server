package com.thebrownfoxx.models.auth

@Serializable
data class Admin(
    val passwordKey: Long,
    val passwordHash: Hash,
)