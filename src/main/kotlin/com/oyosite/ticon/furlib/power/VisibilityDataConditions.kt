package com.oyosite.ticon.furlib.power

import com.oyosite.ticon.furlib.FurLib.MODID
import com.oyosite.ticon.furlib.data.FLSerializableDataTypes.NULL_STRING_CONDITION
import com.oyosite.ticon.furlib.data.FLSerializableDataTypes.NULL_STRING_CONDITIONS
import com.oyosite.ticon.furlib.data.FLSerializableDataTypes.SLOT_FINDER
import com.oyosite.ticon.furlib.data.FLSerializableDataTypes.STRING_CONDITION
import com.oyosite.ticon.furlib.data.FLSerializableDataTypes.STRING_CONDITIONS
import com.oyosite.ticon.furlib.data.FLSerializableDataTypes.VISIBILITY_DATA_CONDITIONS
import com.oyosite.ticon.furlib.data.SDKotlin
import com.oyosite.ticon.furlib.data.SlotFinder
import com.oyosite.ticon.furlib.data.SlotFinderFactory
import com.oyosite.ticon.furlib.util.*
import com.oyosite.ticon.furlib.util.VisibilityData.Companion.biEntity
import io.github.apace100.apoli.data.ApoliDataTypes
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableData.Instance as SDI
import io.github.apace100.calio.data.SerializableDataTypes.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.function.BiFunction

object VisibilityDataConditions {
    private val SerializableData.Instance.inverted get() = getBoolean("inverted")
    val SDKotlin.literal get() = this("literal", STRING, null)
    val SDKotlin.literals get() = this("literals", STRINGS, listOf())
    fun <T> List<T>.anyOrAll(allMatch: Boolean, test: (T)->Boolean): Boolean {return if(allMatch) all(test) else any(test)}
    operator fun invoke(){
        registerStr("constant", SDKotlin()("value", BOOLEAN)) {data, _ -> data.getBoolean("value")}
        registerStr("and", SDKotlin()("conditions", STRING_CONDITIONS, NULL_STRING_CONDITIONS).literals) {data, str ->data.get<List<ApoliCondition<String>>>("conditions").all{it.test(str)} && data.get<List<String>>("literals").all(str::equals)}
        registerStr("or", SDKotlin()("conditions", STRING_CONDITIONS, NULL_STRING_CONDITIONS).literals) {data, str -> data.get<List<ApoliCondition<String>>>("conditions").any{it.test(str)} || data.get<List<String>>("literals").any(str::equals)}
        registerStr("literal", SDKotlin()("value", STRING)) {data, str -> str == data.getString("value")}
        
        registerSF("hand", SDKotlin()("main_hand", BOOLEAN, true), {sdi:SDI, sf:SlotFinder -> sdi["main_hand"] = sf.data as Boolean}) { sdi: SDI -> handSlotFinder(sdi.getBoolean("main_hand"))}
        registerSF("armor", SDKotlin()("slot", INT, -1), {sdi, sf -> sdi["slot"] = sf.data as Int}) {sdi -> SlotFinder(this.registryId, { e -> if(data as Int in 0..3) listOf(e.armorItems.elementAtOrElse(data) { ItemStack.EMPTY }) else listOf(*e.armorItems.toList().toTypedArray()) }, sdi.getInt("slot")) }
        registerSF("player_inv", SDKotlin()("slot", INT, -1), {sdi, sf -> sdi["slot"] = sf.data as Int}) {sdi -> SlotFinder(this.registryId, { e -> if (e is PlayerEntity) if (data as Int in 0 until e.inventory.size()) listOf(e.inventory.getStack(data as Int)) else listOf(*(0 until e.inventory.size()).map{e.inventory.getStack(it)}.toTypedArray()) else listOf(ItemStack.EMPTY)}, sdi.getInt("slot")) }

        register("constant", SDKotlin()("value", BOOLEAN)) {data, _ -> data.getBoolean("value")}
        register("and", SDKotlin()("conditions", VISIBILITY_DATA_CONDITIONS)) {data, vis -> data.get<List<VisibilityDataCondition>>("conditions").all{it.test(vis)}}
        register("or", SDKotlin()("conditions", VISIBILITY_DATA_CONDITIONS)) {data, vis -> data.get<List<VisibilityDataCondition>>("conditions").any{it.test(vis)}}
        register("entities", SDKotlin()("condition", ApoliDataTypes.BIENTITY_CONDITION)) {data, vis -> data.get<BiEntityCondition>("condition").test(vis.biEntity)}
        register("tex", SDKotlin()("condition", STRING_CONDITION, NULL_STRING_CONDITION).literal) { data, vis -> (vis.tex == data.getString("literal") || data.get<ApoliCondition<String>>("condition").test(vis.tex))}
        register("bone", SDKotlin()("condition", STRING_CONDITION, NULL_STRING_CONDITION).literal) { data, vis -> (vis.bone == data.getString("literal") || data.get<ApoliCondition<String>>("condition").test(vis.bone))}
        register("target", SDKotlin()("condition", ApoliDataTypes.ENTITY_CONDITION)) {data, vis -> data.get<ApoliCondition<Entity>>("condition").test(vis.target)}
        register("viewer", SDKotlin()("condition", ApoliDataTypes.ENTITY_CONDITION)) {data, vis -> data.get<ApoliCondition<Entity>>("condition").test(vis.viewer)}
        register("bound_slot", SDKotlin()("bone", STRING_CONDITION, NULL_STRING_CONDITION)("slot", SLOT_FINDER)("condition", ApoliDataTypes.ITEM_CONDITION)("viewer", BOOLEAN, false)("allSlots", BOOLEAN, true)) { data, vis -> data.inverted xor ((!data.isPresent("bone")||data.get<ApoliCondition<String>>("bone").test(vis.bone))&&data.get<SlotFinder>("slot").findSlot(if (data.getBoolean("viewer"))vis.viewer else vis.target).anyOrAll(data.getBoolean("allSlots"),data.get<ApoliCondition<ItemStack>>("condition")::test))}
        /*register("composite", SDKotlin()
            ("tex", STRING_CONDITION, NULL_STRING_CONDITION)
            ("texLiteral", STRINGS, null)
            ("bone", STRING_CONDITION, NULL_STRING_CONDITION)
            ("boneLiteral", STRINGS, null)
            ("entities", ApoliDataTypes.BIENTITY_CONDITION, null)
            ("target", ApoliDataTypes.ENTITY_CONDITION, null)
            ("viewer", ApoliDataTypes.ENTITY_CONDITION, null)
            .invert
        ) {data, vis -> data.inverted xor ((!data.isPresent("tex")||data.get<ApoliCondition<String>>("tex").test(vis.tex))&&(!data.isPresent("texLiteral")||data.get<List<String>>("texLiteral").any(vis.tex::equals))
        ) }*/



    }
    private fun registerSF(id: String, dat: SerializableData, write: SlotFinderFactory.(SDI, SlotFinder) -> Unit, read: SlotFinderFactory.(SDI) -> SlotFinder) = registerSF(Identifier("$MODID:$id"), dat, write, read)
    private fun registerSF(id: Identifier, dat: SerializableData, write: SlotFinderFactory.(SDI, SlotFinder) -> Unit, read: SlotFinderFactory.(SDI) -> SlotFinder) = Registry.register(MiscRegistries.SLOT_FINDER, id, SlotFinderFactory(id, dat, write, read))
    private fun registerStr(id: String, data: SerializableData, condition: (SerializableData.Instance, String) -> Boolean) = registerStr(Identifier("$MODID:$id"), data, condition)
    private fun registerStr(id: Identifier, data: SerializableData, condition: (SerializableData.Instance, String) -> Boolean) = Registry.register(MiscRegistries.STRING_CONDITION, id, ConditionFactory(id, data, BiFunction(condition)))
    private fun register(id: String, data: SerializableData, condition: (SerializableData.Instance, VisibilityData) -> Boolean) = register(Identifier("$MODID:$id"), data, condition)
    private fun register(id: Identifier, data: SerializableData, condition: (SDI, VisibilityData) -> Boolean) = Registry.register(MiscRegistries.VISIBILITY_DATA_CONDITION, id, ConditionFactory(id, data, BiFunction(condition)))
    private fun SlotFinderFactory.handSlotFinder(b: Boolean) = SlotFinder(this.registryId, {listOf(if(it is LivingEntity)if(this.data as Boolean)it.mainHandStack else it.offHandStack else ItemStack.EMPTY)}, b)
}