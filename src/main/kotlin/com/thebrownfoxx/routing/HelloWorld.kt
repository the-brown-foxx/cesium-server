package com.thebrownfoxx.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.helloWorld() {
    get("/") {
        call.respondText("Hello Fucker!")
    }
}