package com.thebrownfoxx.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class UnsavedAdmin(
    val username: String,
    val name: String,
    val passwordHash: Hash,
    val master: Boolean = false,
)

@Serializable
data class SavedAdmin(
    val id: Int,
    val username: String,
    val name: String,
    val passwordHash: Hash,
    val master: Boolean,
    val locked: Boolean,
)