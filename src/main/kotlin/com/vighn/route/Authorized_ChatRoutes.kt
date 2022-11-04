package com.vighn.route


import com.vighn.Authentication.UserDataSource
import com.vighn.room.MemberAlreadyExistEception
import com.vighn.room.RoomController
import com.vighn.session.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import java.lang.Exception

fun Route.chatSocket(roomController: RoomController, userDataSource: UserDataSource){
    authenticate {
        webSocket("/chat-socket") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)
            val user = userDataSource.getUserByUserId(userId ?: "")
//            val session=call.sessions.get<ChatSession>()
//            if(session == null){
//                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY,"no session.."))
//                return@webSocket
//            }
            val username = user?.username ?: ""
            try {

                roomController.onJoin(
                    //userName = session.userName,
                    //sessionID = session.sessionID,
                    userName = username,
                    socket = this
                )

                incoming.consumeEach {frame->
                    if(frame is Frame.Text){
                        roomController.sendMessage(
                            username,
                            frame.readText()
                        )
                    }
                }
            }catch (e : MemberAlreadyExistEception){
                call.respond(HttpStatusCode.Conflict)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
            finally {
                roomController.disconnect(username)
            }

        }
    }

}

fun Route.getAllMessages(roomController: RoomController){
    authenticate {
        get ("/messages") {
            call.respond(HttpStatusCode.OK,roomController.getAllMessage())
        }
    }

}