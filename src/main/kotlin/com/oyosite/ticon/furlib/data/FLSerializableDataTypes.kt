@file:Suppress("MemberVisibilityCanBePrivate")

package com.oyosite.ticon.furlib.data

import com.oyosite.ticon.furlib.client.TexController
import io.github.apace100.calio.data.SerializableDataType as SDT
import io.github.apace100.calio.data.SerializableDataTypes as SDTs

object FLSerializableDataTypes {
    val TEX_CONTROLLER:SDT<TexController> = SDT.compound(TexController::class.java, SDKotlin()("tex", SDTs.IDENTIFIER)("r",SDTs.INT,0)("rm",SDTs.INT,255)("g",SDTs.INT,0)("gm",SDTs.INT,255)("b",SDTs.INT,0)("bm",SDTs.INT,255)("a",SDTs.INT,255)("am",SDTs.INT,255)("illuminate",SDTs.BOOLEAN,false),::TexController,TexController::toData)
    val TEX_CONTROLLERS:SDT<List<TexController>> = SDT.list(TEX_CONTROLLER)
    //operator fun get(s:String) {println(s)}
}