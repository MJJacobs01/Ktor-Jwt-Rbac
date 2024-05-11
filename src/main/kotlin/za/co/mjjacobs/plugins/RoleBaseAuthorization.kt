package za.co.mjjacobs.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 15:55
 */

class PluginConfiguration {
    var roles: Set<String> = emptySet()
}

val RoleBaseAuthorizationPlugin = createRouteScopedPlugin(
    name = "RbacPlugin",
    createConfiguration = ::PluginConfiguration
) {
    val roles = pluginConfig.roles
    
    pluginConfig.apply {
        //  Invoke after authentication checks
        on(AuthenticationChecked) { applicationCall ->
            val tokenRole = getRoleFromToken(applicationCall)
            
            val authorized = roles.contains(tokenRole)
            
            if (!authorized) {
                println("The user does not have a role")
                applicationCall.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}

private fun getRoleFromToken(applicationCall: ApplicationCall): String? = applicationCall.principal<JWTPrincipal>()
    ?.payload
    ?.getClaim("role")
    ?.asString()