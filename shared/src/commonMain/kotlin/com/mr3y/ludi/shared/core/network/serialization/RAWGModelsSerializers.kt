package com.mr3y.ludi.shared.core.network.serialization

import com.mr3y.ludi.shared.core.network.model.DetailedRAWGPlatformInfo
import com.mr3y.ludi.shared.core.network.model.RAWGPlatformInfo
import com.mr3y.ludi.shared.core.network.model.RAWGStoreInfo
import com.mr3y.ludi.shared.core.network.model.ShallowRAWGPlatformInfo
import com.mr3y.ludi.shared.core.network.model.ShallowRAWGStoreInfo
import com.mr3y.ludi.shared.core.network.model.ShallowRAWGStoreInfoWithId
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object RAWGPlatformSerializer : JsonContentPolymorphicSerializer<RAWGPlatformInfo>(RAWGPlatformInfo::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<RAWGPlatformInfo> {
        return when {
            "requirements_en" in element.jsonObject || "released_at" in element.jsonObject -> DetailedRAWGPlatformInfo.serializer()
            else -> ShallowRAWGPlatformInfo.serializer()
        }
    }
}

object RAWGStoreSerializer : JsonContentPolymorphicSerializer<RAWGStoreInfo>(RAWGStoreInfo::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<RAWGStoreInfo> {
        return when {
            "id" in element.jsonObject -> ShallowRAWGStoreInfoWithId.serializer()
            else -> ShallowRAWGStoreInfo.serializer()
        }
    }
}
