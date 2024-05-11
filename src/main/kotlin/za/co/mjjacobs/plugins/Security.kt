package za.co.mjjacobs.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import za.co.mjjacobs.service.*

/**
 * Created by MJ Jacobs on 2024/05/11 at 14:04
 */

fun Application.configureSecurity(jwtService: JwtService) {
    authentication {
        jwt {
            realm = jwtService.realm
            
            verifier(jwtService.jwtVerifier)
            
            validate { credential ->
                jwtService.customValidator(credential)
            }
        }
        
        
        jwt("another-auth") {
            realm = jwtService.realm
            
            verifier(jwtService.jwtVerifier)
            
            validate { credential ->
                jwtService.customValidator(credential)
            }
        }
    }
}