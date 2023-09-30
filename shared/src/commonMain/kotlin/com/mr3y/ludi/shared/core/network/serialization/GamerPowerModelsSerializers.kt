// ktlint-disable filename
package com.mr3y.ludi.shared.core.network.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object NotAvailableAsNullSerializer : KSerializer<String?> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(serialName = "String?", kind = PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String? {
        return when (val value = decoder.decodeString()) {
            "N/A" -> null
            else -> value
        }
    }

    override fun serialize(encoder: Encoder, value: String?) {
        encoder.encodeString(value?.toString() ?: "N/A")
    }
}
