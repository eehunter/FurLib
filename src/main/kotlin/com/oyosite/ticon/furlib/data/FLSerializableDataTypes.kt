@file:Suppress("MemberVisibilityCanBePrivate")

package com.oyosite.ticon.furlib.data

import com.google.gson.JsonArray
import com.oyosite.ticon.furlib.client.TexController
import io.github.apace100.calio.data.SerializableDataType as SDT
import io.github.apace100.calio.data.SerializableDataTypes as SDTs
import io.github.apace100.calio.data.SerializableDataTypes.INT

object FLSerializableDataTypes {
    @Suppress("UNCHECKED_CAST")
    object COLOR:SDT<List<Int>>(List::class.java as Class<List<Int>>,
        {buf, list -> for(n in list)buf.writeInt(n) },
        {buf-> List(8){buf.readInt()} },
        {json -> if(json.isJsonArray) with(json.asJsonArray) { List(8){ if(size()>it)INT.read(get(it)) else 255 } } else List(8) { if(it==3||it==7)255 else INT.read(json) } }
    )
    val TEX_CONTROLLER:SDT<TexController> = SDT.compound(TexController::class.java, SDKotlin()("tex", SDTs.IDENTIFIER)("col", COLOR)("illuminate",SDTs.BOOLEAN,false),::TexController,TexController::toData)
    val TEX_CONTROLLERS:SDT<List<TexController>> = SDT.list(TEX_CONTROLLER)
    //operator fun get(s:String) {println(s)}
}