package com.thebrownfoxx.models.totp

import kotlinx.serialization.Serializable

@Serializable
data class EncryptedBase32(val value: String)