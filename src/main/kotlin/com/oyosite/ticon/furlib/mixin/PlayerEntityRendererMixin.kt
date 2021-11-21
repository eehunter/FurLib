package com.oyosite.ticon.furlib.mixin

import com.oyosite.ticon.furlib.client.SpecieFeature
import com.oyosite.ticon.furlib.power.SpeciePower
import io.github.apace100.apoli.component.PowerHolderComponent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.PlayerEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import net.minecraft.client.network.AbstractClientPlayerEntity as ACPE
import net.minecraft.client.render.entity.EntityRendererFactory.Context as CRFC
import net.minecraft.client.render.entity.model.PlayerEntityModel as PEM

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer::class)
abstract class PlayerEntityRendererMixin(ctx: CRFC, model:PEM<ACPE>, shadowRadius:Float): LivingEntityRenderer<ACPE, PEM<ACPE>>(ctx, model, shadowRadius) {

    @Shadow abstract fun setModelPose(player:ACPE)
    private var _specieFeature:SpecieFeature<ACPE>? = null
    private val specieFeature:SpecieFeature<ACPE>
        get() {return _specieFeature!!}

    @Inject(method = ["setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V"], at = [At("TAIL")], cancellable = true)
    open fun iSetModelPose(p: ACPE, info: CallbackInfo?) {
        if (PowerHolderComponent.KEY.get(p).getPowers(SpeciePower::class.java).size > 0) model.setVisible(false)
    }

    @Inject(method = ["renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V"], at = [At("HEAD")], cancellable = true)
    open fun iRenderArm(matrices:MatrixStack, vertexConsumers: VertexConsumerProvider, light:Int, player: ACPE, arm: ModelPart, sleeve: ModelPart, ci:CallbackInfo?){
        if (PowerHolderComponent.KEY.get(player).getPowers(SpeciePower::class.java).size <= 0) return
        val pem = getModel()
        setModelPose(player)
        pem.handSwingProgress = 0.0f
        pem.sneaking = false
        pem.leaningPitch = 0.0f
        pem.setAngles(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        arm.pitch = 0f
        specieFeature.render(matrices, vertexConsumers, light, player, 0f, 0f, 0f, 0f, 0f, 0f, (if (arm == pem.leftArm) "leftArm" else "rightArm")::equals)
        ci?.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    @Inject(at = [At("RETURN")], method = ["<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory\$Context;Z)V"])
    open fun init(ctx: EntityRendererFactory.Context?, slim: Boolean, ci: CallbackInfo?) {
        _specieFeature = SpecieFeature(this as FeatureRendererContext<ACPE, BipedEntityModel<ACPE>>)
        addFeature(_specieFeature as FeatureRenderer<ACPE, PEM<ACPE>>)
    }


}