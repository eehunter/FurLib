package com.oyosite.ticon.furlib.data

import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataType
import io.github.apace100.calio.data.SerializableDataTypes


class SDKotlin: SerializableData() {
    operator fun invoke(name: String?, type: SerializableDataType<*>?): SDKotlin { add(name,type); return this }
    operator fun <T> invoke(name: String?, type: SerializableDataType<T>?, defaultValue: T): SDKotlin { add(name,type,defaultValue); return this }
    //val invert get() = this("invert", SerializableDataTypes.BOOLEAN, false)
}