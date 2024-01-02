package com.thebrownfoxx.routing

import com.thebrownfoxx.auth.AdminService
import com.thebrownfoxx.auth.logic.*
import com.thebrownfoxx.models.auth.ChangeCredentials
import com.thebrownfoxx.models.auth.JwtConfig
import com.thebrownfoxx.models.auth.LoginCredentials
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoutes(
    jwtConfig: JwtConfig,
    adminService: AdminService,
) {
    login(jwtConfig, adminService)
    changePassword(adminService)
    authenticate(adminService)
}

fun Route.login(
    jwtConfig: JwtConfig,
    adminService: AdminService,
) {
    post("/admin/login") {
        val loginCredentials = call.receiveNullable<LoginCredentials>()
        if (loginCredentials == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val admin = adminService.get()
        if (loginCredentials.password doesNotMatch admin.passwordHash) {
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }
        call.respond(HttpStatusCode.OK, generateJwt(jwtConfig, admin.passwordKey))
    }
}

fun Route.changePassword(adminService: AdminService) {
    authenticatedPatch("/admin/password", adminService) {
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
    authenticatedGet("/admin/authenticate", adminService) {
        call.respond(HttpStatusCode.OK, "Authenticated")
    }
}