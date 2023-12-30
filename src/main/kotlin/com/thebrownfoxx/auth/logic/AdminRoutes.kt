package com.thebrownfoxx.auth.logic

import com.thebrownfoxx.auth.AdminService
import com.thebrownfoxx.auth.models.JWTConfig
import com.thebrownfoxx.auth.models.LoginCredentials
import com.thebrownfoxx.auth.models.UnsavedAdmin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
        val admin = adminService.getByUsername(credentials.username)
        if (admin == null) {
            call.respond(HttpStatusCode.NotFound)
            return@post
        }
        if (credentials.password.verify(admin.passwordHash)) {
            call.respond(HttpStatusCode.OK, generateJWT(jwtConfig, admin.id))
            return@post
        }
        call.respond(HttpStatusCode.Unauthorized)
    }
}

fun Route.addAdmin(adminService: AdminService) {
    authenticate {
        post("/admin") {
            val addingAdminId = call.principal<JWTPrincipal>()?.getUsername()?.toInt()
            if (addingAdminId == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            val addingAdmin = adminService.get(addingAdminId)
            if (addingAdmin == null) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }
            if (!addingAdmin.master) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            val admin = call.receiveNullable<UnsavedAdmin>()
            if (admin == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            adminService.add(admin)
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.authenticate() {
    authenticate {
        get("/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}