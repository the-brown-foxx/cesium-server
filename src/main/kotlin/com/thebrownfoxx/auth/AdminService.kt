package com.thebrownfoxx.auth

import com.thebrownfoxx.auth.models.Admin
import com.thebrownfoxx.auth.models.Hash

interface AdminService {
    suspend fun get(): Admin
    suspend fun update(newPasswordHash: Hash)
}