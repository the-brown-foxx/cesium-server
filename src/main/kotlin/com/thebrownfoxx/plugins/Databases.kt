package com.thebrownfoxx.plugins

import com.thebrownfoxx.auth.AdminService
import com.thebrownfoxx.auth.logic.authenticate
import com.thebrownfoxx.auth.logic.login
import com.thebrownfoxx.auth.models.JWTConfig
import com.thebrownfoxx.auth.models.UnsavedAdmin
import com.thebrownfoxx.totp.AccessorService
import com.thebrownfoxx.totp.models.UnsavedAccessor
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureDatabases(
    jwtConfig: JWTConfig,
    adminService: AdminService,
    accessorService: AccessorService,
) {
    routing {
        login(jwtConfig, adminService)
        authenticate()
//        get("/users") {
//            call.respond(HttpStatusCode.OK, adminService.getAll())
//        }
//        post("/users") {
//            val user = call.receive<UnsavedAdmin>()
//            val savedUser = adminService.add(user)
//            call.respond(HttpStatusCode.Created, savedUser)
//        }
//        delete("/users/delete/{username}") {
//            val username = call.parameters["username"] ?: throw IllegalArgumentException("Invalid username")
//            adminService.delete(username)
//            call.respond(HttpStatusCode.OK)
//        }
//        get("/accessors") {
//            call.respond(HttpStatusCode.OK, accessorService.getAll())
//        }
//        post("/accessors") {
//            val accessor = call.receive<UnsavedAccessor>()
//            val savedAccessor = accessorService.add(accessor)
//            call.respond(HttpStatusCode.Created, savedAccessor)
//        }
//        delete("/accessors/delete/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            accessorService.delete(id)
//            call.respond(HttpStatusCode.OK)
//        }
    }
}
