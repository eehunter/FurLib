package com.oyosite.ticon.furlib.mixin;

import com.oyosite.ticon.furlib.client.SpecieFeature;
import com.oyosite.ticon.furlib.power.SpeciePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {super(ctx, model, shadowRadius);}

    private SpecieFeature<AbstractClientPlayerEntity> specieFeature = null;

    @Shadow protected abstract void setModelPose(AbstractClientPlayerEntity player);
    @Inject(method = "setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("TAIL"))
    void iSetModelPose(AbstractClientPlayerEntity p, CallbackInfo ci){
        if (PowerHolderComponent.KEY.get(p).getPowers(SpeciePower.class).size() > 0) model.setVisible(false);
    }
    @Inject(method = "renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V", at = @At("HEAD"), cancellable = true)
    void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci){
        if (PowerHolderComponent.KEY.get(player).getPowers(SpeciePower.class).size() <= 0) return;
        PlayerEntityModel<AbstractClientPlayerEntity> pem = getModel();
        setModelPose(player);
        pem.handSwingProgress = 0.0f;
        pem.sneaking = false;
        pem.leaningPitch = 0.0f;
        pem.setAngles(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        arm.pitch = 0f;
        specieFeature.render(matrices, vertexConsumers, light, player, 0f, 0f, 0f, 0f, 0f, 0f, ((arm == pem.leftArm)?"leftArm":"rightArm")::equals);
        ci.cancel();
    }
    @SuppressWarnings("unchecked")
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V")
    void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci){
        specieFeature = new SpecieFeature<>((FeatureRendererContext<AbstractClientPlayerEntity, BipedEntityModel<AbstractClientPlayerEntity>>) (Object) this);
        addFeature((FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>)(Object)specieFeature);
    }
}
