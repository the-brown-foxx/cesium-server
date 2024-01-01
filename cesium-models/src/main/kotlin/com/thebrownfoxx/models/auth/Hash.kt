package com.thebrownfoxx.models.auth

@Serializable
data class Hash(val value: Base64, val salt: Base64)