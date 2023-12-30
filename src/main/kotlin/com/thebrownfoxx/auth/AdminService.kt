package com.thebrownfoxx.auth

import com.thebrownfoxx.auth.models.SavedAdmin
import com.thebrownfoxx.auth.models.UnsavedAdmin

interface AdminService {
    suspend fun getAll(): List<SavedAdmin>
    suspend fun get(id: Int): SavedAdmin?
    suspend fun getByUsername(username: String): SavedAdmin?
    suspend fun add(admin: UnsavedAdmin): SavedAdmin
    suspend fun update(admin: SavedAdmin)
    suspend fun delete(id: Int)
}