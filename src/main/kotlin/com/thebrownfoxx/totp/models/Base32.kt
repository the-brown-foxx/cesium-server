package com.thebrownfoxx.totp.models

import kotlinx.serialization.Serializable

@Serializable
data class Base32(val value: String)