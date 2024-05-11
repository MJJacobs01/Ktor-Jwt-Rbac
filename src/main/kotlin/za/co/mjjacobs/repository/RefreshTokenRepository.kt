package za.co.mjjacobs.repository

/**
 * Created by MJ Jacobs on 2024/05/11 at 14:35
 */

class RefreshTokenRepository {
    
    //  Replace with database in production
    private val tokens = mutableMapOf<String, String>()
    
    fun findUsernameByToken(token: String): String? {
        return tokens[token]
    }
    
    fun save(
        token: String,
        username: String
    ) {
        tokens[token] = username
    }
}