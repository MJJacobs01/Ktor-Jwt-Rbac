package za.co.mjjacobs.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import za.co.mjjacobs.routing.request.*
import java.util.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:50
 */

class JwtService(
    private val application: Application,
    private val userService: UserService
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
    
    fun createJwtToken(loginRequest: LoginRequest): String? {
        val foundUser = userService.findByUsername(username = loginRequest.username)
        
        return if (foundUser != null && foundUser.password == loginRequest.password) {
            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", foundUser.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC512(secret))
        } else null
    }
    
    fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val username = extractUsername(credential)
        val foundUser = username?.let(userService::findByUsername)
        
        return foundUser?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }
    
    private fun audienceMatches(credential: JWTCredential): Boolean {
        return credential.payload.audience.contains(audience)
    }
    
    private fun extractUsername(credential: JWTCredential): String? {
        return credential.payload.getClaim("username").asString()
    }
    
    private fun getConfigProperty(path: String) = application.environment.config.property(path).getString()
}