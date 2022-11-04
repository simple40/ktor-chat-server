package com.vighn.Authentication

import com.vighn.Authentication.Model.SaltedHash

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom


class SHA256HashService : HashService {
    override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val hexSalt = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$hexSalt$value")
        return SaltedHash(
            hash = hash,
            salt = hexSalt
        )
    }

    override fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt+value) == saltedHash.hash
    }
}