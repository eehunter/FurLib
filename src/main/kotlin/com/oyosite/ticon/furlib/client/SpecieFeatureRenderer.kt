package com.oyosite.ticon.furlib.client

import com.oyosite.ticon.furlib.FurLib
import com.oyosite.ticon.furlib.power.SpeciePower
import net.minecraft.client.MinecraftClient
import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.geo.render.built.GeoModel
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.model.provider.GeoModelProvider
import software.bernie.geckolib3.renderers.geo.IGeoRenderer
import software.bernie.geckolib3.util.GeoUtils


class SpecieFeatureRenderer<E:LivingEntity> : BipedEntityModel<E>(getModelData(Dilation.NONE, 0f).root.createPart(64, 64)),IGeoRenderer<SpeciePower> {

    private var headBone = "head"
    private var bodyBone = "torso"
    private var rightArmBone = "rightArm"
    private var leftArmBone = "leftArm"
    private var rightLegBone = "rightLeg"
    private var leftLegBone = "leftLeg"

    private var gmp = GMP()
    override fun getGeoModelProvider(): GeoModelProvider<SpeciePower> { return gmp }
    override fun getTextureLocation(instance: SpeciePower?): Identifier { return FurLib.identifier("none") }
    fun render (stack: MatrixStack,vcp: VertexConsumerProvider,light: Int,entity:E,ctxModel:BipedEntityModel<E>,sp: SpeciePower, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        stack.push()
        stack.translate(0.0, 24 / 16.0, 0.0)
        stack.scale(-1.0f, -1.0f, 1.0f)
        val model: GeoModel = gmp.getModel(sp.getModelLocation())
        val animEvent:AnimationEvent<SpeciePower> = AnimationEvent(sp,limbAngle,limbDistance,tickDelta,false,listOf())
        gmp.setLivingAnimations(sp, this.getUniqueID(sp), animEvent);
        fitToBiped(ctxModel)
        for (tex in sp.texControllers) {
            MinecraftClient.getInstance().textureManager.bindTexture(tex.tex)
            val vc: VertexConsumer = vcp.getBuffer(RenderLayer.getEntityCutoutNoCull(tex.tex))
            val renderType = getRenderType(sp, tickDelta, stack, vcp, vc, light, tex.tex)
            render(model, sp, tickDelta, renderType, stack, vcp, vc, if (tex.illuminate) 15728640 else light, OverlayTexture.DEFAULT_UV, tex.r/255f, tex.g/255f, tex.b/255f, tex.a/255f)
        }
        stack.pop()
    }
    private fun fitBoneToBiped(bone: String?, yOffset: Int, xOffset: Int, copyFrom: ModelPart, modelProvider: AnimatedGeoModel<*>) {
        if (bone == null) return
        val iBone = modelProvider.getBone(bone)
        GeoUtils.copyRotations(copyFrom, iBone)
        iBone.positionX = copyFrom.pivotX + xOffset
        iBone.positionY = yOffset - copyFrom.pivotY
        iBone.positionZ = copyFrom.pivotZ
    }
    private fun fitToBiped(ctxModel: BipedEntityModel<E>) {
        fitBoneToBiped(this.headBone, 0, 0, ctxModel.head, gmp)
        fitBoneToBiped(this.bodyBone, 0, 0, ctxModel.body, gmp)
        fitBoneToBiped(this.rightArmBone, 2, 5, ctxModel.rightArm, gmp)
        fitBoneToBiped(this.leftArmBone, 2, -5, ctxModel.leftArm, gmp)
        fitBoneToBiped(this.rightLegBone, 12, 2, ctxModel.rightLeg, gmp)
        fitBoneToBiped(this.leftLegBone, 12, -2, ctxModel.leftLeg, gmp)
    }
    private class GMP:AnimatedGeoModel<SpeciePower>() {
        override fun getModelLocation(sp: SpeciePower): Identifier { return sp.getModelLocation() }
        override fun getTextureLocation(sp: SpeciePower): Identifier { return sp.getTextureLocation() }
        override fun getAnimationFileLocation(sp: SpeciePower): Identifier { return sp.getAnimFileLocation()?:FurLib.identifier("none") }
    }
}