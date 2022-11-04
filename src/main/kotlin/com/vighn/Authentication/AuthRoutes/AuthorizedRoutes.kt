package com.vighn.Authentication.AuthRoutes

import com.vighn.Authentication.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getSecretInfo(userDateSource: UserDataSource) {
    authenticate {
        get("secret"){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)
            val user = userDateSource.getUserByUserId(userId ?: "")
            call.respond(HttpStatusCode.OK , "Your id is ${user?.username}")
        }
    }
}

fun Route.authenticating(userDateSource: UserDataSource) {
    authenticate {
        get("authenticate") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)
            val user = userDateSource.getUserByUserId(userId ?: "")
            call.respond(HttpStatusCode.OK,message="${user?.username}")
        }
    }
}