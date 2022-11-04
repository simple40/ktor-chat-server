package com.vighn.Authentication

import com.mongodb.client.model.InsertOneOptions
import com.vighn.Authentication.Model.User
import org.bson.types.ObjectId
import org.litote.kmongo.bson
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserDateSourceImpl(
    private val db : CoroutineDatabase
    ) : UserDataSource {

    val users=db.getCollection<User>()
    override suspend fun getUserByUsername(username: String): User? {
        return users.findOne(User::username eq username)
    }

    override suspend fun insertNewUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
    override suspend fun checkIfUsernameTaken(username: String) :Boolean{
        if(users.findOne(User::username eq username) !=null)
            return true
        return false
    }

    override suspend fun getUserByUserId(userId: String): User? {
        return users.findOne(User::id eq ObjectId(userId))
    }
}