@file:Suppress("MemberVisibilityCanBePrivate")

package com.oyosite.ticon.furlib.data

import com.oyosite.ticon.furlib.client.TexController
import io.github.apace100.calio.data.SerializableDataType as SDT
import io.github.apace100.calio.data.SerializableDataTypes as SDTs

object FLSerializableDataTypes {
    val TEX_CONTROLLER:SDT<TexController> = SDT.compound(TexController::class.java, SDKotlin()("tex", SDTs.IDENTIFIER)("r",SDTs.FLOAT,1f)("g",SDTs.FLOAT,1f)("b",SDTs.FLOAT,1f)("a",SDTs.FLOAT,1f),::TexController,TexController::toData)
    val TEX_CONTROLLERS:SDT<List<TexController>> = SDT.list(TEX_CONTROLLER)
    //operator fun get(s:String) {println(s)}
}