package com.thebrownfoxx.totp.models

import com.thebrownfoxx.auth.models.SavedAdmin
import kotlinx.serialization.Serializable

@Serializable
data class UnsavedAccessor(
    val name: String,
    val totpSecret: EncryptedBase32,
    val admin: SavedAdmin? = null, // TODO: This shouldn't be nullable
)

@Serializable
data class SavedAccessor(
    val id: Int,
    val name: String,
    val totpSecret: EncryptedBase32,
    val admin: SavedAdmin? = null, // TODO: This shouldn't be nullable
)
