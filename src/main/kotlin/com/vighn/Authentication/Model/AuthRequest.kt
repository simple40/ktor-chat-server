package com.vighn.Authentication.Model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username : String,
    val password : String
)
