package com.thebrownfoxx.totp.models

import kotlinx.serialization.Serializable

@Serializable
data class EncryptedBase32(val value: String)