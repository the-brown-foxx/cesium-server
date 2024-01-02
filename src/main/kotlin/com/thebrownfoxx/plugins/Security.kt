package com.thebrownfoxx.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.thebrownfoxx.models.auth.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(jwtConfig: JwtConfig) {
    // Please read the jwt property from the config file if you are using EngineMain
    authentication {
        jwt {
            realm = jwtConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfig.secret))
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.audience))
                    JWTPrincipal(credential.payload) else null
            }
        }
    }
}
