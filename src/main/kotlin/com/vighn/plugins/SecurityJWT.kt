package com.vighn.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.vighn.Authentication.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurityJWT(config : TokenConfig){
    authentication {
        jwt {
            realm = config.realm
            verifier(
                JWT
                .require(Algorithm.HMAC256(config.secret))
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .build())
            validate {credential->
                if (credential.payload.audience.contains(config.audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge{defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized,"Token is not valid or expired")
                call.respondRedirect("/signIN")
            }
        }
    }
}
