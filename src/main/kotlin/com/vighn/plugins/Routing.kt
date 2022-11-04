package com.vighn.plugins

import com.vighn.Authentication.AuthRoutes.authenticating
import com.vighn.Authentication.AuthRoutes.getSecretInfo
import com.vighn.Authentication.AuthRoutes.signIN
import com.vighn.Authentication.AuthRoutes.signUP
import com.vighn.Authentication.HashService
import com.vighn.Authentication.TokenConfig
import com.vighn.Authentication.TokenService
import com.vighn.Authentication.UserDataSource
import com.vighn.room.RoomController
import com.vighn.route.chatSocket
import com.vighn.route.getAllMessages
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.koin.ktor.ext.inject

fun Application.configureRouting(config : TokenConfig) {
    val roomController by inject<RoomController>()
    val userDataSource by inject<UserDataSource>()
    val hashService by inject<HashService> ()
    val tokenService by inject<TokenService>()

    routing {
        chatSocket(roomController,userDataSource)
        getAllMessages(roomController)
        signIN(userDataSource,hashService,tokenService,config)
        signUP(hashService,userDataSource)
        getSecretInfo(userDataSource)
        authenticating(userDataSource)
        get("/") {
            call.respond(HttpStatusCode.OK,"hello")
        }
    }
}
