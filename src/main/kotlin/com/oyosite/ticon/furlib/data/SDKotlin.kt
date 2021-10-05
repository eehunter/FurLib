package com.oyosite.ticon.furlib.data

import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataType




class SDKotlin: SerializableData() {
    operator fun invoke(name: String?, type: SerializableDataType<*>?): SDKotlin { add(name,type); return this }
    operator fun <T> invoke(name: String?, type: SerializableDataType<T>?, defaultValue: T): SDKotlin { add(name,type,defaultValue); return this }
}