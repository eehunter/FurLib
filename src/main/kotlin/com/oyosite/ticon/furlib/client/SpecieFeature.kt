package com.oyosite.ticon.furlib.client

import com.oyosite.ticon.furlib.FurLibClient
import com.oyosite.ticon.furlib.power.SpeciePower
import io.github.apace100.apoli.component.PowerHolderComponent
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity

class SpecieFeature<T:LivingEntity>(private val context: FeatureRendererContext<T, BipedEntityModel<T>>): FeatureRenderer<T, BipedEntityModel<T>>(context) {
    private val defaultSpecieRenderer = SpecieFeatureRenderer<T>()
    override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, entity: T, limbAngle: Float, limbDistance: Float, tickDelta: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {
        val sps: List<SpeciePower> = PowerHolderComponent.KEY.get(entity).getPowers(SpeciePower::class.java)
        val sp: SpeciePower = if(sps.isNotEmpty())sps[0] else return
        val renderer = defaultSpecieRenderer
        renderer.render(matrices,vertexConsumers,light,entity,contextModel,sp,limbAngle,limbDistance,tickDelta)
    }
}