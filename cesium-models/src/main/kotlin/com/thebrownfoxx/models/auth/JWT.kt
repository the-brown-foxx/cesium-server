package com.thebrownfoxx.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class JWT(val value: String)