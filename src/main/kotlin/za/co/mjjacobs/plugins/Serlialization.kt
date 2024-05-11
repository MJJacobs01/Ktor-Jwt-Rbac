package za.co.mjjacobs.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:18
 */

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}