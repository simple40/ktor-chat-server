package com.vighn.Authentication.Model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token : String
)
