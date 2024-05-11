package za.co.mjjacobs.repository

import za.co.mjjacobs.model.*
import java.util.UUID

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:04
 */

class UserRepository {
    
    //  Production app this will be your database
    private val users = mutableListOf<User>()
    
    fun findAll(): List<User> = users
    
    fun findById(id: UUID): User? =
        users.firstOrNull { it.id == id }
    
    fun findByUsername(username: String): User? =
        users.firstOrNull { it.username == username }
    
    fun save(user: User): Boolean =
        users.add(user)
}