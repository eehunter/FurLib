@file:Suppress("UNCHECKED_CAST")

package com.oyosite.ticon.furlib.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.oyosite.ticon.furlib.client.TexColor
import com.oyosite.ticon.furlib.power.SpeciePower
import io.github.apace100.apoli.component.PowerHolderComponent
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.command.CommandManager.*
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate


object Commands:CommandRegistrationCallback {
    val commands: MutableList<Any> = mutableListOf()
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>, dedicated: Boolean) {
        val cmd = literal("furlib")
        commands.forEach{cmd.then(it as ArgumentBuilder<ServerCommandSource, *>)}
        dispatcher.register(cmd)
    }
    init {
        commands.add(literal("getColor").requires(Predicate(::hasSpeciePower)).then(argument("texture",StringArgumentType.string()).suggests(SuggestionProvider(::texSuggestion)).executes(Command(::getColor))))
        commands.add(literal("setColor").requires(Predicate(::hasSpeciePower)).then(argument("texture",StringArgumentType.string()).suggests(SuggestionProvider(::texSuggestion)).then(argument("color",StringArgumentType.string()).executes(Command(::setColor)))))
    }
    private fun texSuggestion(ctx:CommandContext<ServerCommandSource>, builder:SuggestionsBuilder): CompletableFuture<Suggestions>? {
        PowerHolderComponent.KEY.get(ctx.source.entity!!).getPowers(SpeciePower::class.java)[0].texControllers.forEach { builder.suggest(it.alias) }
        return builder.buildFuture()
    }
    private fun hasSpeciePower(src:ServerCommandSource):Boolean{
        if(src.entity == null)return false
        return PowerHolderComponent.KEY.get(src.entity!!).getPowers(SpeciePower::class.java).isNotEmpty()
    }
    private fun getColor(ctx:CommandContext<ServerCommandSource>):Int{
        val entity = ctx.source.entity!!
        val tex = ctx.getArgument("texture", String::class.java)
        val specie = PowerHolderComponent.KEY.get(entity).getPowers(SpeciePower::class.java)[0]
        if (specie.texControllers.map { it.alias }.none(tex::equals)) throw SimpleCommandExceptionType(TranslatableText("furlib.get_color.invalid_texture", tex)).create()
        val texCtrl = specie.texControllers.find {it.alias == tex}!!
        ctx.source.sendFeedback(TranslatableText("furlib.get_color.success", tex, texCtrl.col.toString()), false)
        return 1
    }
    private fun setColor(ctx:CommandContext<ServerCommandSource>):Int{
        val entity = ctx.source.entity!!
        val tex = ctx.getArgument("texture", String::class.java)
        val specie = PowerHolderComponent.KEY.get(entity).getPowers(SpeciePower::class.java)[0]
        if (specie.texControllers.map { it.alias }.none(tex::equals)) throw SimpleCommandExceptionType(TranslatableText("furlib.set_color.invalid_texture", tex)).create()
        val texCtrl = specie.texControllers.find {it.alias == tex}!!
        val oldCol = texCtrl.col.toString()
        val col = TexColor.parseColor(ctx.getArgument("color",String::class.java))
        texCtrl.col.copy(col)
        PowerHolderComponent.syncPower(entity, specie.type)
        ctx.source.sendFeedback(TranslatableText("furlib.set_color.success",tex,oldCol,texCtrl.col.toString()), false)
        return 1
    }
}