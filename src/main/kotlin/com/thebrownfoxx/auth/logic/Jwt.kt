package com.thebrownfoxx.auth.logic

import com.auth0.jwt.algorithms.Algorithm
import com.thebrownfoxx.models.auth.Jwt
import com.thebrownfoxx.models.auth.JwtClaim
import com.thebrownfoxx.models.auth.JwtConfig
import io.ktor.server.auth.jwt.*
import java.util.*
import com.auth0.jwt.JWT as JWTBuilder

const val PASSWORD_KEY_CLAIM_KEY = "password_key"

fun generateJwt(
    config: JwtConfig,
    vararg claims: JwtClaim,
): Jwt {
    var jwt = JWTBuilder
        .create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withExpiresAt(Date(System.currentTimeMillis() + config.validity.inWholeMilliseconds))
    for (claim in claims) {
        jwt = jwt.withClaim(claim.key, claim.value)
    }
    return Jwt(jwt.sign(Algorithm.HMAC256(config.secret)))
}

fun generateJwt(
    config: JwtConfig,
    passwordKey: Long,
) = generateJwt(config, JwtClaim(PASSWORD_KEY_CLAIM_KEY, passwordKey.toString()))

fun JWTPrincipal.getPasswordKey() = getClaim(PASSWORD_KEY_CLAIM_KEY, String::class)?.toLong()