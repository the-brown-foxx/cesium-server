package com.thebrownfoxx

import com.thebrownfoxx.auth.ExposedAdminService
import com.thebrownfoxx.auth.models.JWTConfig
import com.thebrownfoxx.plugins.*
import com.thebrownfoxx.totp.ExposedAccessorService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import kotlin.time.Duration.Companion.days

fun main() {
    embeddedServer(Netty, port = 8069, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val jwtConfig = JWTConfig(
        realm = "ktor sample app",
        issuer = "https://jwt-provider-domain/",
        audience = "jwt-audience",
        validity = 30.days,
        secret = "secret",
    )

    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    val userService = ExposedAdminService(database)
    val accessorService = ExposedAccessorService(database)

    configureSecurity(jwtConfig)
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting(jwtConfig, userService, accessorService)
}
