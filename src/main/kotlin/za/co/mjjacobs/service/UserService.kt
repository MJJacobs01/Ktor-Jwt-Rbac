package za.co.mjjacobs.service

import za.co.mjjacobs.model.*
import za.co.mjjacobs.repository.*
import java.util.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:07
 */

class UserService(private val userRepository: UserRepository) {
    
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
}