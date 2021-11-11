package com.oyosite.ticon.furlib
import com.oyosite.ticon.furlib.power.FLPowerFactories
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import kotlin.random.Random

object FurLib: ModInitializer {
    const val MODID = "furlib"
    private const val RAND_SEED = 123456789
    val RAND = Random(RAND_SEED)
    override fun onInitialize() {
        FLPowerFactories()
    }
    fun identifier(id:String):Identifier{ return Identifier(MODID,id) }
}
