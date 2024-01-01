package com.thebrownfoxx.models.totp

import kotlinx.serialization.Serializable

@Serializable
data class Base32(val value: String)