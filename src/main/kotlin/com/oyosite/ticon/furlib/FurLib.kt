package com.oyosite.ticon.furlib
import com.oyosite.ticon.furlib.power.FLPowerFactories
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object FurLib: ModInitializer {
    const val MODID = "furlib"
    override fun onInitialize() {
        FLPowerFactories()
    }
    fun identifier(id:String):Identifier{ return Identifier(MODID,id) }
}
