package com.thebrownfoxx.routing

import com.thebrownfoxx.auth.AdminService
import com.thebrownfoxx.auth.logic.*
import com.thebrownfoxx.auth.models.ChangeCredentials
import com.thebrownfoxx.auth.models.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoutes(
    jwtConfig: JWTConfig,
    adminService: AdminService,
) {
    login(jwtConfig, adminService)
    changePassword(adminService)
    authenticate(adminService)
}

fun Route.login(
    jwtConfig: JWTConfig,
    adminService: AdminService,
) {
    post("/login") {
        val password = call.receiveNullable<String>()
        if (password == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val admin = adminService.get()
        if (!(password matches admin.passwordHash)) {
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }
        call.respond(HttpStatusCode.OK, generateJWT(jwtConfig, admin.passwordKey))
    }
}

fun Route.changePassword(adminService: AdminService) {
    authenticatedPatch("/change-password", adminService) {
        val credentials = call.receiveNullable<ChangeCredentials>()
        if (credentials == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedPatch
        }
        val admin = adminService.get()
        println(credentials.oldPassword.hash(admin.passwordHash.salt))
        println(admin.passwordHash)
        if (credentials.oldPassword doesNotMatch admin.passwordHash) {
            call.respond(HttpStatusCode.Unauthorized)
            return@authenticatedPatch
        }
        if (credentials.newPassword.length < 8) {
            call.respond(HttpStatusCode.BadRequest, "Password must be at least 8 characters long")
            return@authenticatedPatch
        }
        adminService.update(credentials.newPassword.hash())
        call.respond(HttpStatusCode.OK, "Password changed")
    }
}

fun Route.authenticate(adminService: AdminService) {
    authenticatedGet("/authenticate", adminService) {
        call.respond(HttpStatusCode.OK)
    }
}