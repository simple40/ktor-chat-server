package com.vighn.Authentication.Model

data class SaltedHash(
    val hash : String,
    val salt : String
)
