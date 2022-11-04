package com.vighn.room

import com.vighn.Authentication.UserDataSource
import com.vighn.Data.MessageDataSource
import com.vighn.Data.Model.Message
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String,Member>()

    fun onJoin(
        userName: String,
        //sessionID: String,
        socket: WebSocketSession
    ){
        if(members.containsKey(userName))
            throw MemberAlreadyExistEception()

        members[userName]= Member(
            userName=userName,
            //sessionID=sessionID,
            socket=socket
        )
    }

    suspend fun sendMessage(
        userName: String,
        message: String
    ){
        val messageEntity=Message(
            userName=userName,
            text = message,
            timeStamp = System.currentTimeMillis()
        )
        messageDataSource.insertMessage(messageEntity)
        val parsedMessage= Json.encodeToString(messageEntity)

        members.values.forEach{ member->
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessage(): List<Message> {
        return messageDataSource.getAllMessages()
    }

    suspend fun disconnect(userName: String){
        members[userName]?.socket?.close()
        if(members.containsKey(userName)){
            members.remove(userName)
        }
    }

}