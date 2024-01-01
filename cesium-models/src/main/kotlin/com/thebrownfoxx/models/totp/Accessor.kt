package com.thebrownfoxx.models.totp

import kotlinx.serialization.Serializable

@Serializable
data class UnsavedAccessor(
    val name: String,
    val totpSecret: EncryptedBase32,
)

@Serializable
data class SavedAccessor(
    val id: Int,
    val name: String,
    val totpSecret: EncryptedBase32,
)
