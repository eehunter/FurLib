package com.oyosite.ticon.furlib.power

import com.oyosite.ticon.furlib.client.TexController
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class SpeciePower(t:PowerType<*>, entity:LivingEntity, private val id:Identifier, val texControllers: List<TexController>):Power(t, entity),IAnimatable {
    private val animFactory = AnimationFactory(this)
    val currentTexIndex = 0
    override fun registerControllers(animData: AnimationData?) {}
    override fun getFactory(): AnimationFactory { return animFactory }
    fun getModelLocation():Identifier{return id}
    fun getTextureLocation(t:Int = currentTexIndex):Identifier{return texControllers[t].tex}
    fun getAnimFileLocation():Identifier?{return null}
}