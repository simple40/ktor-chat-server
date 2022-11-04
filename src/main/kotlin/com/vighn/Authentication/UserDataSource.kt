package com.vighn.Authentication

import com.vighn.Authentication.Model.User

interface UserDataSource {
    suspend fun getUserByUsername(username: String) : User?
    suspend fun insertNewUser(user: User) : Boolean
    suspend fun checkIfUsernameTaken(username: String) :Boolean
    suspend fun getUserByUserId(userId : String) : User?
}