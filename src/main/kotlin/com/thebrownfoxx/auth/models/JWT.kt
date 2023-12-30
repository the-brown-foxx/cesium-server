package com.thebrownfoxx.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class JWT(val value: String)