package com.vighn.room

import io.ktor.websocket.*

data class Member(
    val userName: String,
    //val sessionID: String,
    val socket: WebSocketSession
)
