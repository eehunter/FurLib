@file:Suppress("UNCHECKED_CAST")

package com.oyosite.ticon.furlib.power
import com.oyosite.ticon.furlib.FurLib
import com.oyosite.ticon.furlib.client.TexController
import com.oyosite.ticon.furlib.data.FLSerializableDataTypes
import com.oyosite.ticon.furlib.data.SDKotlin
import io.github.apace100.apoli.power.PowerType
import io.github.apace100.apoli.power.factory.PowerFactory
import io.github.apace100.apoli.registry.ApoliRegistries
import io.github.apace100.calio.data.SerializableData
import io.github.apace100.calio.data.SerializableDataTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.util.registry.Registry
import java.util.function.BiFunction

object FLPowerFactories {
    operator fun invoke() {
        register(PowerFactory(FurLib.identifier("specie"), SDKotlin()("model", SerializableDataTypes.IDENTIFIER)("textures",FLSerializableDataTypes.TEX_CONTROLLERS)) { data: SerializableData.Instance -> BiFunction { type: PowerType<SpeciePower>, entity: LivingEntity -> SpeciePower(type, entity, data.getId("model"), data["textures"] as List<TexController>) } })
    }
    private fun register(serializer: PowerFactory<*>) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.serializerId, serializer)
    }
}