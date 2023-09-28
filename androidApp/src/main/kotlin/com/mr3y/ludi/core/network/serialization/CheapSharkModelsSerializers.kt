package com.mr3y.ludi.core.network.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object StringAsIntSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(serialName = "StringAsIntSerializer", kind = PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Int {
        return decoder.decodeString().toInt()
    }

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeString(value.toString())
    }
}

object StringAsLongSerializer : KSerializer<Long> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(serialName = "StringAsLongSerializer", kind = PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Long {
        return decoder.decodeString().toLong()
    }

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeString(value.toString())
    }
}

object StringAsFloatSerializer : KSerializer<Float> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(serialName = "StringAsFloatSerializer", kind = PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Float {
        return decoder.decodeString().toFloat()
    }

    override fun serialize(encoder: Encoder, value: Float) {
        encoder.encodeString(value.toString())
    }
}

object StringAsDoubleSerializer : KSerializer<Double> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(serialName = "StringAsDoubleSerializer", kind = PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Double {
        return decoder.decodeString().toDouble()
    }

    override fun serialize(encoder: Encoder, value: Double) {
        encoder.encodeString(value.toString())
    }
}

object StringAsBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(serialName = "StringAsBooleanSerializer", kind = PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Boolean {
        return decoder.decodeString().let { it == "1" }
    }

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeString(if (value) "1" else "0")
    }
}
