package za.co.mjjacobs.service

import com.auth0.jwt.interfaces.DecodedJWT
import za.co.mjjacobs.model.*
import za.co.mjjacobs.repository.*
import za.co.mjjacobs.routing.request.*
import za.co.mjjacobs.routing.response.*
import java.util.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:07
 */

class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    
    fun findAll(): List<User> =
        userRepository.findAll()
    
    fun findById(id: String): User? =
        userRepository.findById(UUID.fromString(id))
    
    fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)
    
    fun saveUser(user: User): User? {
        val foundUser = findByUsername(user.username)
        
        return if (foundUser == null) {
            userRepository.save(user)
            user
        } else null
    }
    
    fun authenticate(loginRequest: LoginRequest): AuthResponse? {
        val username = loginRequest.username
        val foundUser = userRepository.findByUsername(username)
        
        return if (foundUser != null && foundUser.password == loginRequest.password) {
            val accessToken = jwtService.createAccessToken(username, foundUser.role)
            val refreshToken = jwtService.createRefreshToken(username, foundUser.role)
            refreshTokenRepository.save(refreshToken, username)
            
            AuthResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } else null
    }
    
    fun refreshToken(token: String): String? {
        val decodedRefreshToken = verifyRefreshToken(token)
        val persistedUsername = refreshTokenRepository.findUsernameByToken(token)
        
        return if (decodedRefreshToken != null && persistedUsername != null) {
            val foundUser = userRepository.findByUsername(username = persistedUsername)
            val usernameFromRefreshToken = decodedRefreshToken.getClaim("username").asString()
            
            if (foundUser != null && usernameFromRefreshToken == foundUser.username) {
                jwtService.createAccessToken(persistedUsername, foundUser.role)
            } else null
        } else null
    }
    
    private fun verifyRefreshToken(token: String): DecodedJWT? {
        val decodedJwt = decodedJWT(token)
        
        return decodedJwt?.let {
            val audienceMatches = jwtService.audienceMatches(it.audience.first())
            
            if (audienceMatches) {
                decodedJwt
            } else null
        }
    }
    
    private fun decodedJWT(token: String) = try {
        jwtService.jwtVerifier.verify(token)
    } catch (e: Exception) {
        null
    }
}