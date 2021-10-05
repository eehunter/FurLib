package com.oyosite.ticon.furlib.power

import com.oyosite.ticon.furlib.client.TexController
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier

class SpeciePower(t:PowerType<*>,entity:LivingEntity,val id:Identifier, val texControllers: List<TexController>):Power(t, entity) {
    //fun render()
}