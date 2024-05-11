package za.co.mjjacobs.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import za.co.mjjacobs.repository.*
import za.co.mjjacobs.routing.request.*
import java.util.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:50
 */

class JwtService(
    private val application: Application,
    private val userRepository: UserRepository
) {
    private val secret = getConfigProperty("jwt.secret")
    private val audience = getConfigProperty("jwt.audience")
    private val issuer = getConfigProperty("jwt.issuer")
    val realm = getConfigProperty("jwt.realm")
    
    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC512(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
    
    fun createAccessToken(username: String): String {
        return createJwtToken(username, 3_600_00)
    }
    
    fun createRefreshToken(username: String): String {
        return createJwtToken(username, 86_400_000)
    }
    
    private fun createJwtToken(
        username: String,
        expireIn: Int
    ): String {
        return JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + expireIn))
            .sign(Algorithm.HMAC512(secret))
    }
    
    fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val username = extractUsername(credential)
        val foundUser = username?.let(userRepository::findByUsername)
        
        return foundUser?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }
    
    fun audienceMatches(audience: String): Boolean {
        return this.audience == audience
    }
    
    private fun audienceMatches(credential: JWTCredential): Boolean {
        return credential.payload.audience.contains(audience)
    }
    
    private fun extractUsername(credential: JWTCredential): String? {
        return credential.payload.getClaim("username").asString()
    }
    
    private fun getConfigProperty(path: String) = application.environment.config.property(path).getString()
}