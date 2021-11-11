package com.oyosite.ticon.furlib.power

import com.oyosite.ticon.furlib.client.TexController
import io.github.apace100.apoli.power.Power
import io.github.apace100.apoli.power.PowerType
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class SpeciePower(t:PowerType<*>, entity:LivingEntity, private val id:Identifier, var texControllers: List<TexController>):Power(t, entity),IAnimatable {
    private val animFactory = AnimationFactory(this)
    val currentTexIndex = 0
    override fun registerControllers(animData: AnimationData?) {}
    override fun getFactory(): AnimationFactory { return animFactory }
    fun getModelLocation():Identifier{return id}
    fun getTextureLocation(t:Int = currentTexIndex):Identifier{return texControllers[t].tex}
    fun getAnimFileLocation():Identifier?{return null}
    override fun fromTag(nbt: NbtElement?) {
        if (nbt !is NbtCompound) return
        if (!nbt.contains("tex")) return
        val tex = nbt.getCompound("tex")
        texControllers = List(tex.getInt("len")) { i -> TexController(tex.getCompound(i.toString())) }
    }
    override fun toTag(): NbtElement {
        val nbt = NbtCompound()
        val tex = NbtCompound()
        tex.putInt("len",texControllers.size)
        texControllers.forEachIndexed { i, t -> tex.put(i.toString(),t.serialize()) }
        nbt.put("tex",tex)
        return nbt
    }
}