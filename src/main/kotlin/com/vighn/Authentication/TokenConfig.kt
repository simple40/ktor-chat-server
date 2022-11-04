package com.vighn.Authentication

data class TokenConfig(
    val secret : String ,
    val issuer : String ,
    val audience : String ,
    val realm : String,
    val expireIN : Long
)
