package com.thebrownfoxx.plugins

import com.thebrownfoxx.auth.AdminService
import com.thebrownfoxx.models.auth.JWTConfig
import com.thebrownfoxx.routing.accessorRoutes
import com.thebrownfoxx.routing.adminRoutes
import com.thebrownfoxx.totp.AccessorService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    jwtConfig: JWTConfig,
    adminService: AdminService,
    accessorService: AccessorService,
) {
    routing {
        adminRoutes(jwtConfig, adminService)
        accessorRoutes(adminService, accessorService)
    }
}
