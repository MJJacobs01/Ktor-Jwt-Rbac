package za.co.mjjacobs.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import za.co.mjjacobs.routing.request.*
import za.co.mjjacobs.routing.response.*
import za.co.mjjacobs.service.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 14:10
 */

fun Route.authRoute(
    userService: UserService
) {
    
    post {
        val loginRequest = call.receive<LoginRequest>()
        
        val authResponse: AuthResponse? = userService.authenticate(loginRequest)
        
        authResponse?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = it
            )
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
    
    post("/refresh") {
        val request = call.receive<RefreshTokenRequest>()
        
        val newAccessToken: String? = userService.refreshToken(request.token)
        
        newAccessToken?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = RefreshTokenRequest(token = it)
            )
        } ?: call.respond(
            message = HttpStatusCode.Unauthorized
        )
    }
}