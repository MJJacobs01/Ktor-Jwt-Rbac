package za.co.mjjacobs.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import za.co.mjjacobs.model.*
import za.co.mjjacobs.routing.request.*
import za.co.mjjacobs.routing.response.*
import za.co.mjjacobs.service.*
import java.util.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:22
 */

fun Route.userRoute(userService: UserService) {
    post {
        val userRequest = call.receive<UserRequest>()
        
        val createdUser = userService.saveUser(
            user = userRequest.toModel()
        ) ?: return@post call.respond(HttpStatusCode.BadRequest)
        
        call.respond(
            status = HttpStatusCode.Created,
            message = createdUser.id.toString()
        )
    }
    
    authenticate {
        get {
            val users = userService.findAll()
            
            call.respond(
                status = HttpStatusCode.OK,
                message = users.map(User::toResponse)
            )
        }
    }
    
    authenticate("another-auth") {
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            
            val foundUser = userService.findById(id = id) ?: return@get call.respond(HttpStatusCode.NotFound)
            
            if (foundUser.username != extractPrincipalUsername(call)) {
                return@get call.respond(HttpStatusCode.NotFound)
            }
            
            call.respond(
                status = HttpStatusCode.OK,
                message = foundUser.toResponse()
            )
        }
    }
}

fun extractPrincipalUsername(call: ApplicationCall): String? {
    return call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("username")
        ?.asString()
}

private fun UserRequest.toModel(): User {
    return User(
        id = UUID.randomUUID(),
        username = username,
        password = password
    )
}

private fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id,
        username = username
    )
}
