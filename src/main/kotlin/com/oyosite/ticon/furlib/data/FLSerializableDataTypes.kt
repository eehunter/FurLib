@file:Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package com.oyosite.ticon.furlib.data

import com.google.gson.JsonElement
import com.oyosite.ticon.furlib.FurLib.MODID
import com.oyosite.ticon.furlib.client.TexColor
import com.oyosite.ticon.furlib.client.TexController
import com.oyosite.ticon.furlib.power.FLConditionTypes
import com.oyosite.ticon.furlib.util.*
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.condition.ConditionType
import net.minecraft.util.Identifier
import java.util.function.BiConsumer
import io.github.apace100.calio.data.SerializableDataType as SDT
import io.github.apace100.calio.data.SerializableDataTypes as SDTs

object FLSerializableDataTypes {
    //@Suppress("UNCHECKED_CAST")
    //object COLOR:SDT<List<Int>>(List::class.java as Class<List<Int>>, {buf, list -> for(n in list)buf.writeInt(n) }, {buf-> List(8){buf.readInt()} }, {json -> if(json.isJsonArray) with(json.asJsonArray) { List(8){ if(size()>it)INT.read(get(it)) else 255 } } else List(8) { if(it==3||it==7)255 else INT.read(json) } })
    object COLOR:SDT<TexColor>(TexColor::class.java, TexColor::send, TexColor::receive, TexColor::read)
    val TEX_CONTROLLER:SDT<TexController> = SDT.compound(TexController::class.java, SDKotlin()("alias", SDTs.STRING, null)("tex", SDTs.IDENTIFIER)("col", COLOR)("illuminate",SDTs.BOOLEAN,false),::TexController,TexController::toData)
    val TEX_CONTROLLERS:SDT<List<TexController>> = SDT.list(TEX_CONTROLLER)
    val VISIBILITY_DATA_CONDITION: SDT<VisibilityDataCondition> = condition(FLConditionTypes.VISIBILITY_DATA)
    val VISIBILITY_DATA_CONDITIONS: SDT<List<VisibilityDataCondition>> = SDT.list(VISIBILITY_DATA_CONDITION)
    val STRING_CONDITION: SDT<ApoliCondition<String>> = condition(FLConditionTypes.STRING)
    val STRING_CONDITIONS: SDT<List<ApoliCondition<String>>> = SDT.list(STRING_CONDITION)
    val STRINGS: SDT<List<String>> = SDT.list(SDTs.STRING)
    val SLOT_FINDER = SDT(SlotFinder::class.java, {buf, sf -> buf.writeIdentifier(sf.factory);MiscRegistries.SLOT_FINDER[sf.factory]!!.write(buf, sf)}, {buf -> MiscRegistries.SLOT_FINDER[buf.readIdentifier()]!!.read(buf)}) {json: JsonElement -> MiscRegistries.SLOT_FINDER[Identifier(json.asJsonObject["type"].asString)]!!.read(json.asJsonObject)}

    val NULL_STRING_CONDITION = FLConditionTypes.STRING.read("{type:\"$MODID:constant\",value:true}")
    val NULL_STRING_CONDITIONS = STRING_CONDITIONS.read("[{type:\"$MODID:constant\",value:true}]")

    fun <T> condition(type: ConditionType<T>) = ApoliDataTypes.condition(conditionClassOf(), type)
}