package com.thebrownfoxx.totp

import com.thebrownfoxx.totp.models.SavedAccessor
import com.thebrownfoxx.totp.models.UnsavedAccessor

interface AccessorService {
    suspend fun getAll(): List<SavedAccessor>
    suspend fun get(id: Int): SavedAccessor?
    suspend fun add(accessor: UnsavedAccessor): SavedAccessor
    suspend fun update(accessor: SavedAccessor)
    suspend fun delete(id: Int)
}