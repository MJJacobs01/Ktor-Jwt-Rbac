package za.co.mjjacobs.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import za.co.mjjacobs.routing.request.*
import za.co.mjjacobs.service.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 14:10
 */

fun Route.authRoute(
    jwtService: JwtService
) {
    
    post {
        val loginRequest = call.receive<LoginRequest>()
        
        val token = jwtService.createJwtToken(loginRequest = loginRequest)
        
        token?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = hashMapOf("token" to it)
            )
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
}