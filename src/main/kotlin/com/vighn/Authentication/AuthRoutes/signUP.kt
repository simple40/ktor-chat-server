package com.vighn.Authentication.AuthRoutes

import com.vighn.Authentication.HashService
import com.vighn.Authentication.Model.AuthRequest
import com.vighn.Authentication.Model.User
import com.vighn.Authentication.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUP(
    hashService: HashService,
    userDataSource: UserDataSource
){
    post("sighUP") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val isFieldBlank =request.password.isBlank() || request.username.isBlank()
        val isPWshort = request.password.length < 8

        if(isFieldBlank || isPWshort){
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        if(userDataSource.checkIfUsernameTaken(request.username)){
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        val saltedHash = hashService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        val wasAcknowledged = userDataSource.insertNewUser(user)
        if(!wasAcknowledged){
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }



}