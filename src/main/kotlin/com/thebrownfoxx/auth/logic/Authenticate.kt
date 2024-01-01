package com.thebrownfoxx.auth.logic

import com.thebrownfoxx.auth.AdminService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.authenticated(adminService: AdminService): Boolean {
    val principal = call.principal<JWTPrincipal>()
    val admin = adminService.get()
    return principal?.getPasswordKey() == admin.passwordKey
}

fun Route.authenticatedPost(
    path: String,
    adminService: AdminService,
    block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit,
) {
    authenticate {
        post(path) {
            if (!authenticated(adminService)) {
                call.respond(HttpStatusCode.Unauthorized)
                return@post
            }
            block()
        }
    }
}

fun Route.authenticatedGet(
    path: String,
    adminService: AdminService,
    block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit,
) {
    authenticate {
        get(path) {
            if (!authenticated(adminService)) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }
            block()
        }
    }
}

fun Route.authenticatedDelete(
    path: String,
    adminService: AdminService,
    block: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit,
) {
    authenticate {
        delete(path) {
            if (!authenticated(adminService)) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }
            block()
        }
    }
}