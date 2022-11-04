package com.vighn.Authentication.AuthRoutes

import com.vighn.Authentication.HashService
import com.vighn.Authentication.Model.AuthRequest
import com.vighn.Authentication.Model.AuthResponse
import com.vighn.Authentication.Model.SaltedHash
import com.vighn.Authentication.Model.TokenClaim
import com.vighn.Authentication.TokenConfig
import com.vighn.Authentication.TokenService
import com.vighn.Authentication.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signIN(
    userDataSource: UserDataSource,
    hashService: HashService,
    tokenService: TokenService,
    config: TokenConfig
){
    post("signIN") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val isFieldBlank =request.password.isBlank() || request.username.isBlank()
        val isPWshort = request.password.length < 8

        if(isFieldBlank || isPWshort){
            call.respond(HttpStatusCode.Conflict,"Incorrect credentials")
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username)
        if(user == null){
            call.respond(HttpStatusCode.Conflict,"Incorrect credentials")
            return@post
        }

        val verified = hashService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if(!verified){
           call.respond(HttpStatusCode.Conflict,"Incorrect Credentials")
        }

        val token = tokenService.generateToken(
            config = config,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(HttpStatusCode.OK,
        message = AuthResponse(
            token = token
        )
        )

    }
}