package com.vighn.Authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.vighn.Authentication.Model.TokenClaim
import java.util.Date

class JwtTokenService : TokenService {
    override fun generateToken(config: TokenConfig, vararg tokenClaims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expireIN))

        tokenClaims.forEach {claim->
            token=token.withClaim(
                claim.name,claim.value
            )
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}