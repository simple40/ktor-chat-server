package com.vighn.Authentication

import com.vighn.Authentication.Model.TokenClaim

interface TokenService {
    fun generateToken(
        config : TokenConfig,
        vararg tokenClaims: TokenClaim
    ) : String
}