package com.oyosite.ticon.furlib.client

import com.oyosite.ticon.furlib.FurLib
import com.oyosite.ticon.furlib.power.SpeciePower
import com.oyosite.ticon.furlib.util.MCPair
import com.oyosite.ticon.furlib.util.VisibilityData
import io.github.apace100.apoli.component.PowerHolderComponent
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
import software.bernie.geckolib3.geo.render.built.GeoBone
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

    private val bones = mutableListOf(headBone,bodyBone,rightArmBone,leftArmBone,rightLegBone,leftLegBone)

    private var ctxModel: BipedEntityModel<E>? = null
    private var entity: E? = null
    private var currentTexture: String = ""

    private var gmp = GMP()
    override fun getGeoModelProvider(): GeoModelProvider<SpeciePower> { return gmp }
    override fun getTextureLocation(instance: SpeciePower?): Identifier { return FurLib.identifier("none") }

    private var boneVis: ((String)->Boolean)? = null

    fun render(stack: MatrixStack,vcp: VertexConsumerProvider,light: Int,entity:E,ctxModel:BipedEntityModel<E>,sp: SpeciePower, limbAngle: Float, limbDistance: Float, tickDelta: Float, boneVis: ((String)->Boolean)?) {
        stack.push()
        stack.translate(0.0, 24 / 16.0, 0.0)
        stack.scale(-1.0f, -1.0f, 1.0f)
        val model: GeoModel = gmp.getModel(sp.getModelLocation())
        val animEvent:AnimationEvent<SpeciePower> = AnimationEvent(sp,limbAngle,limbDistance,tickDelta,false,listOf())
        gmp.setLivingAnimations(sp, this.getUniqueID(sp), animEvent)
        this.ctxModel = ctxModel
        this.entity = entity
        //fitToBiped(ctxModel,sp)
        this.boneVis = boneVis
        for (tex in sp.texControllers) {
            currentTexture = tex.alias
            MinecraftClient.getInstance().textureManager.bindTexture(tex.tex)
            val vc: VertexConsumer = vcp.getBuffer(RenderLayer.getEntityCutoutNoCull(tex.tex))
            val renderType = getRenderType(sp, tickDelta, stack, vcp, vc, light, tex.tex)
            render(model, sp, tickDelta, renderType, stack, vcp, vc, if (tex.illuminate) 15728640 else light, getOverlay(entity), tex.col.r.toFloat()/255f, tex.col.g.toFloat()/255f, tex.col.b.toFloat()/255f, tex.col.a.toFloat()/255f)
        }
        stack.pop()
    }

    fun getOverlay(entity: E) = OverlayTexture.packUv(OverlayTexture.getU(0f),OverlayTexture.getV(entity.hurtTime>0||entity.deathTime>0))

    override fun renderRecursively(bone: GeoBone, stack: MatrixStack, bufferIn: VertexConsumer, packedLightIn: Int, packedOverlayIn: Int, red: Float, green: Float, blue: Float, alpha: Float) {
        fitBoneToBiped(bone.name, ctxModel, PowerHolderComponent.KEY.get(entity!!).getPowers(SpeciePower::class.java)[0])
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
    }
    private fun fitBoneToBiped(bone: String?, yOffset: Int, xOffset: Int, copyFrom: ModelPart, modelProvider: AnimatedGeoModel<*>, p:Boolean) {
        bone ?: return
        val iBone = modelProvider.getBone(bone)
        iBone.isHidden = !p
        if(iBone.isHidden) return
        GeoUtils.copyRotations(copyFrom, iBone)
        iBone.positionX = copyFrom.pivotX + xOffset
        iBone.positionY = yOffset - copyFrom.pivotY
        iBone.positionZ = copyFrom.pivotZ
    }
    private fun fitBoneToBiped(bone: String?, ctxModel: BipedEntityModel<E>?, sp: SpeciePower){
        bone ?: return
        ctxModel ?: return
        val p = sp.boneVisibilityCache[VisibilityData(currentTexture, bone, entity!!, MinecraftClient.getInstance().player!!)] && (boneVis?.invoke(bone) ?: true)
        when (bone){
            headBone -> fitBoneToBiped(this.headBone, 0, 0, ctxModel.head, gmp, p)
            bodyBone -> fitBoneToBiped (this.bodyBone, 0, 0, ctxModel.body, gmp, p)
            rightArmBone -> fitBoneToBiped(this.rightArmBone, 2, 5, ctxModel.rightArm, gmp, p)
            leftArmBone -> fitBoneToBiped(this.leftArmBone, 2, -5, ctxModel.leftArm, gmp, p)
            rightLegBone -> fitBoneToBiped (this.rightLegBone, 12, 2, ctxModel.rightLeg, gmp, p)
            leftLegBone -> fitBoneToBiped(this.leftLegBone, 12, -2, ctxModel.leftLeg, gmp, p)
            else -> gmp.getBone(bone).isHidden = !p
        }

    }
    //private fun fitToBiped(ctxModel: BipedEntityModel<E>, sp: SpeciePower) = bones.forEach { fitBoneToBiped(it, ctxModel, sp) }

    private class GMP:AnimatedGeoModel<SpeciePower>() {
        override fun getModelLocation(sp: SpeciePower): Identifier { return sp.getModelLocation() }
        override fun getTextureLocation(sp: SpeciePower): Identifier { return sp.getTextureLocation() }
        override fun getAnimationFileLocation(sp: SpeciePower): Identifier { return sp.getAnimFileLocation()?:FurLib.identifier("none") }
    }
}