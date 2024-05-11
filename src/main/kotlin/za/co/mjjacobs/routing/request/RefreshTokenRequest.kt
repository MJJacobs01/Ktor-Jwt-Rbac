package za.co.mjjacobs.routing.request

import kotlinx.serialization.Serializable

/**
 * Created by MJ Jacobs on 2024/05/11 at 14:43
 */

@Serializable
data class RefreshTokenRequest(
    val token: String
)