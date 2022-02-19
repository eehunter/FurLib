@file:Suppress("UNCHECKED_CAST")

package com.oyosite.ticon.furlib.util

import com.oyosite.ticon.furlib.FurLib.MODID
import com.oyosite.ticon.furlib.data.SlotFinderFactory
import io.github.apace100.apoli.power.factory.condition.ConditionFactory
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object MiscRegistries {
    val VISIBILITY_DATA_CONDITION: Registry<ConditionFactory<VisibilityData>> = FabricRegistryBuilder.createSimple(ConditionFactory::class.java as Class<ConditionFactory<VisibilityData>>, Identifier("$MODID:visibility_data_condition")).buildAndRegister()
    val STRING_CONDITION: Registry<ConditionFactory<String>> = FabricRegistryBuilder.createSimple(ConditionFactory::class.java as Class<ConditionFactory<String>>, Identifier("$MODID:string_condition")).buildAndRegister()
    val SLOT_FINDER: Registry<SlotFinderFactory> = FabricRegistryBuilder.createSimple(SlotFinderFactory::class.java, Identifier("$MODID:slot_finder_factory")).buildAndRegister()
}