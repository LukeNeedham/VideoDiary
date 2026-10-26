package com.lukeneedham.videodiary.ui.navigation.normal

import android.net.Uri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

/** Saves a [LocalDate] as its ISO text (`2025-01-31`), so a [Route] holding one can be saved and restored. */
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor = PrimitiveSerialDescriptor("java.time.LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString())
}

/** Saves a [Uri] as its text, so a [Route] holding one can be saved and restored. */
object UriSerializer : KSerializer<Uri> {
    override val descriptor = PrimitiveSerialDescriptor("android.net.Uri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): Uri = Uri.parse(decoder.decodeString())
}
