package za.co.mjjacobs.routing.response

import kotlinx.serialization.Serializable
import za.co.mjjacobs.util.*
import java.util.UUID

@Serializable
data class UserResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String
)
