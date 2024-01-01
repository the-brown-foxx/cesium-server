package com.thebrownfoxx.auth.logic

import com.thebrownfoxx.auth.AdminService
import com.thebrownfoxx.auth.models.ChangeCredentials
import com.thebrownfoxx.auth.models.JWTConfig
import com.thebrownfoxx.auth.models.LoginCredentials
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.login(
    jwtConfig: JWTConfig,
    adminService: AdminService,
) {
    post("/login") {
        val credentials = call.receiveNullable<LoginCredentials>()
        if (credentials == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val admin = adminService.get()
        if (!(credentials.password matches admin.passwordHash)) {
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }
        call.respond(HttpStatusCode.OK, generateJWT(jwtConfig, admin.passwordKey))
    }
}

fun Route.changePassword(adminService: AdminService) {
    authenticatedPost("/change-password", adminService) {
        val credentials = call.receiveNullable<ChangeCredentials>()
        if (credentials == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedPost
        }
        val admin = adminService.get()
        println(credentials.oldPassword.hash(admin.passwordHash.salt))
        println(admin.passwordHash)
        if (credentials.oldPassword doesNotMatch admin.passwordHash) {
            call.respond(HttpStatusCode.Unauthorized)
            return@authenticatedPost
        }
        if (credentials.newPassword.length < 8) {
            call.respond(HttpStatusCode.BadRequest, "Password must be at least 8 characters long")
            return@authenticatedPost
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