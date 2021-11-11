package com.oyosite.ticon.furlib.mixin

import com.oyosite.ticon.furlib.client.SpecieFeature
import com.oyosite.ticon.furlib.power.SpeciePower
import io.github.apace100.apoli.component.PowerHolderComponent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.PlayerEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import net.minecraft.client.network.AbstractClientPlayerEntity as ACPE
import net.minecraft.client.render.entity.EntityRendererFactory.Context as CRFC
import net.minecraft.client.render.entity.model.PlayerEntityModel as PEM

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer::class)
abstract class PlayerEntityRendererMixin(ctx: CRFC, model:PEM<ACPE>, shadowRadius:Float): LivingEntityRenderer<ACPE, PEM<ACPE>>(ctx, model, shadowRadius) {
    @Inject(method = ["setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V"], at = [At("TAIL")], cancellable = true)
    open fun setModelPose(p: ACPE, info: CallbackInfo?) {
        if (PowerHolderComponent.KEY.get(p).getPowers(SpeciePower::class.java).size > 0) model.setVisible(false)
    }
    @Suppress("UNCHECKED_CAST")
    @Inject(at = [At("RETURN")], method = ["<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory\$Context;Z)V"])
    open fun init(ctx: EntityRendererFactory.Context?, slim: Boolean, ci: CallbackInfo?) {
        val specieFeature = SpecieFeature(this as FeatureRendererContext<ACPE, BipedEntityModel<ACPE>>)
        addFeature(specieFeature as FeatureRenderer<ACPE, PEM<ACPE>>)
    }
}