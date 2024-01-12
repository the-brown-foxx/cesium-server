@file:Suppress("DuplicatedCode")

package com.thebrownfoxx.routing

import com.thebrownfoxx.auth.AdminService
import com.thebrownfoxx.auth.logic.authenticatedDelete
import com.thebrownfoxx.auth.logic.authenticatedGet
import com.thebrownfoxx.auth.logic.authenticatedPatch
import com.thebrownfoxx.auth.logic.authenticatedPost
import com.thebrownfoxx.models.totp.AccessorInfo
import com.thebrownfoxx.totp.AccessorService
import com.thebrownfoxx.totp.logic.decrypt
import com.thebrownfoxx.totp.logic.newAccessor
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import kotlin.time.Duration.Companion.seconds

fun Route.accessorRoutes(
    adminService: AdminService,
    accessorService: AccessorService,
) {
    getAccessors(adminService, accessorService)
    getAccessor(adminService, accessorService)
    addAccessor(adminService, accessorService)
    updateAccessorName(adminService, accessorService)
    refreshAccessorTotpSecret(adminService, accessorService)
    deleteAccessor(adminService, accessorService)
    authenticateAccessor(accessorService)
}

fun Route.getAccessors(
    adminService: AdminService,
    accessorService: AccessorService,
) {
    authenticatedGet("/accessors", adminService) {
        call.respond(HttpStatusCode.OK, accessorService.getAll())
    }
}

fun Route.getAccessor(
    adminService: AdminService,
    accessorService: AccessorService,
) {
    authenticatedGet("/accessors/{id}", adminService) {
        val id = call.parameters["id"]?.toInt()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedGet
        }
        val accessor = accessorService.get(id)
        if (accessor == null) {
            call.respond(HttpStatusCode.NotFound)
            return@authenticatedGet
        }
        call.respond(HttpStatusCode.OK, accessor)
    }
}

fun Route.addAccessor(
    adminService: AdminService,
    accessorService: AccessorService,
) {
    authenticatedPost("/accessors", adminService) {
        val accessorInfo = call.receiveNullable<AccessorInfo>()
        if (accessorInfo == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedPost
        }
        if (accessorInfo.name.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Name cannot be blank")
            return@authenticatedPost
        }
        val accessor = accessorService.add(newAccessor(accessorInfo.name))
        call.respond(HttpStatusCode.Created, accessor)
    }
}

fun Route.updateAccessorName(
    adminService: AdminService,
    accessorService: AccessorService,
) {
    authenticatedPatch("/accessors/{id}/name", adminService) {
        val id = call.parameters["id"]?.toInt()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedPatch
        }
        val accessorInfo = call.receiveNullable<AccessorInfo>()
        if (accessorInfo == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedPatch
        }
        if (accessorInfo.name.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Name cannot be blank")
            return@authenticatedPatch
        }
        accessorService.updateName(id, accessorInfo.name)
        call.respond(HttpStatusCode.OK, "Accessor name updated")
    }
}

fun Route.refreshAccessorTotpSecret(
    adminService: AdminService,
    accessorService: AccessorService,
) {
    authenticatedPatch("/accessors/{id}/totp-secret", adminService) {
        val id = call.parameters["id"]?.toInt()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedPatch
        }
        accessorService.refreshTotpSecret(id)
        call.respond(HttpStatusCode.OK, "Accessor TOTP secret refreshed")
    }
}

fun Route.deleteAccessor(
    adminService: AdminService,
    accessorService: AccessorService,
) {
    authenticatedDelete("/accessors/{id}", adminService) {
        val id = call.parameters["id"]?.toInt()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@authenticatedDelete
        }
        accessorService.delete(id)
        call.respond(HttpStatusCode.OK, "Accessor deleted")
    }
}

fun Route.authenticateAccessor(accessorService: AccessorService) {
    get("/accessors/{id}/{totp}") {
        val id = call.parameters["id"]?.toInt()
        val totp = call.parameters["totp"]
        if (id == null || totp == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val accessor = accessorService.get(id)
        if (accessor == null) {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        val x = accessor.totpSecret.decrypt().value
        val googleAuthenticator = GoogleAuthenticator(base32secret = x.encodeToByteArray())
        val lastTotp = googleAuthenticator.generate(Date(System.currentTimeMillis() - 30.seconds.inWholeMilliseconds))
        val currentTotp = googleAuthenticator.generate()
        if (totp == currentTotp || totp == lastTotp) {
            call.respond(HttpStatusCode.OK, "Authenticated")
        } else {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}