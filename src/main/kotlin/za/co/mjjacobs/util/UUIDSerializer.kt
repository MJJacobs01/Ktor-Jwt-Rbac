package za.co.mjjacobs.util

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.UUID

/**
 * Created by MJ Jacobs on 2024/05/11 at 13:14
 */

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
    
    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
    
    override fun serialize(
        encoder: Encoder,
        value: UUID
    ) {
        encoder.encodeString(value.toString())
    }
}