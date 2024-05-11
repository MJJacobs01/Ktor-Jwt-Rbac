package za.co.mjjacobs.model

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val password: String,
    val role: String         //  Can be of any type that is comparable
)
