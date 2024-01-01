package com.thebrownfoxx.auth

import com.thebrownfoxx.models.auth.Admin
import com.thebrownfoxx.models.auth.Hash

interface AdminService {
    suspend fun get(): Admin
    suspend fun update(newPasswordHash: Hash)
}