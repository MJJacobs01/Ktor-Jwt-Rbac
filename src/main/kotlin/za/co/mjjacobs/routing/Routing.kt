package za.co.mjjacobs.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*
import za.co.mjjacobs.service.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:19
 */

fun Application.configureRouting(
    userService: UserService,
    jwtService: JwtService
) {
    
    routing {
        route("/api/auth") {
            authRoute(jwtService = jwtService)
        }
        
        route("/api/user") {
            userRoute(userService)
        }
    }
}