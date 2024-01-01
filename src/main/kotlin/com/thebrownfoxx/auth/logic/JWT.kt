package com.thebrownfoxx.auth.logic

import com.auth0.jwt.algorithms.Algorithm
import com.thebrownfoxx.auth.models.JWT
import com.thebrownfoxx.auth.models.JWTClaim
import com.thebrownfoxx.auth.models.JWTConfig
import java.util.*
import com.auth0.jwt.JWT as JWTBuilder

const val ADMIN_ID_CLAIM = "username"

fun generateJWT(
    config: JWTConfig,
    vararg claims: JWTClaim,
): JWT {
    var jwt = JWTBuilder
        .create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withExpiresAt(Date(System.currentTimeMillis() + config.validity.inWholeMilliseconds))
    for (claim in claims) {
        jwt = jwt.withClaim(claim.key, claim.value)
    }
    return JWT(jwt.sign(Algorithm.HMAC256(config.secret)))
}

fun generateJWT(
    config: JWTConfig,
    adminId: Int = 0,
) = generateJWT(config, JWTClaim(ADMIN_ID_CLAIM, adminId.toString()))