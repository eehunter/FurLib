package com.oyosite.ticon.furlib
import com.oyosite.ticon.furlib.command.Commands
import com.oyosite.ticon.furlib.power.FLPowerFactories
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.util.Identifier
import kotlin.random.Random

object FurLib: ModInitializer {
    const val MODID = "furlib"
    private const val RAND_SEED = 123456789
    val RAND = Random(RAND_SEED)
    override fun onInitialize() {
        FLPowerFactories()
        CommandRegistrationCallback.EVENT.register(Commands)
    }
    fun identifier(id:String):Identifier{ return Identifier(MODID,id) }
}
