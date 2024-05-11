package za.co.mjjacobs

import io.ktor.server.application.*
import za.co.mjjacobs.plugins.*
import za.co.mjjacobs.repository.*
import za.co.mjjacobs.routing.*
import za.co.mjjacobs.service.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    val jwtService = JwtService(
        application = this,
        userService = userService
    )
    
    configureSerialization()
    configureSecurity(jwtService = jwtService)
    configureRouting(userService = userService, jwtService = jwtService)
}
