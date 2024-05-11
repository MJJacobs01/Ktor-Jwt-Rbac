package za.co.mjjacobs.util

import io.ktor.server.application.*
import io.ktor.server.routing.*
import za.co.mjjacobs.plugins.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 16:01
 */

fun Route.authorized(
    vararg hasAnyRole: String,
    build: Route.() -> Unit
) {
    install(RoleBaseAuthorizationPlugin) {
        roles = hasAnyRole.toSet()
    }
    build()
}