package com.vighn.Authentication

import com.vighn.Authentication.Model.SaltedHash

interface HashService {
    fun generateSaltedHash(value : String, saltLength : Int = 32) : SaltedHash
    fun verify(value : String, saltedHash: SaltedHash) : Boolean
}