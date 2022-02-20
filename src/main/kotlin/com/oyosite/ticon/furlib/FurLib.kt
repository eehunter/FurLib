package com.oyosite.ticon.furlib
import com.oyosite.ticon.furlib.command.Commands
import com.oyosite.ticon.furlib.power.FLPowerFactories
import com.oyosite.ticon.furlib.power.VisibilityDataConditions
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.util.Identifier
import software.bernie.geckolib3.GeckoLib

object FurLib: ModInitializer {
    const val MODID = "furlib"
    override fun onInitialize() {
        GeckoLib.initialize()
        VisibilityDataConditions()
        FLPowerFactories()
        CommandRegistrationCallback.EVENT.register(Commands)
    }
    fun identifier(id:String):Identifier{ return Identifier(MODID,id) }
}
